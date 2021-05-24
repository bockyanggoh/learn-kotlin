package com.example.jpacodeval

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.annotation.PostConstruct
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.transaction.Transactional

@SpringBootApplication
open class JpaCodevalApplication(
    @Autowired val codeValueDatabaseService: CodeValueDatabaseService
)
fun main(args: Array<String>) {
    runApplication<JpaCodevalApplication>(*args)
}

@Entity
class CodeType(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
    @Enumerated(value = EnumType.STRING)
    @Column(unique = true)
    var name: CodeTypeEnum,
    @OneToMany(mappedBy = "codeType", cascade = [CascadeType.ALL], orphanRemoval = true)
    var codeValues: MutableList<CodeValue> = ArrayList()
)

@Entity
class CodeValue(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) var id: Long? = null,
    @ManyToOne
    @JoinColumn(name="code_type_id")
    var codeType: CodeType,
    @Enumerated(value = EnumType.STRING)
    @Column(unique = true)
    var name: CodeValueEnum,
)

@Repository
interface CodeTypeRepo: JpaRepository<CodeType, String> {
    fun findCodeTypeByName(name: CodeTypeEnum): CodeType?
}

@Repository
interface CodeValueRepo: JpaRepository<CodeValue, String> {

    fun findCodeValueByCodeType_NameAndName(type: CodeTypeEnum, value: CodeValueEnum): CodeValue?
    fun findCodeValuesByCodeType_NameAndNameIn(type: CodeTypeEnum, value: List<CodeValueEnum>): List<CodeValue>?

}


@Service
class CodeValueDatabaseService(
    @Autowired val codeTypeRepo: CodeTypeRepo,
    @Autowired val codeValueRepo: CodeValueRepo,
) {
    @Transactional
    fun idempotent(type: CodeTypeEnum, value: CodeValueEnum): CodeValue {
        val retrieved = codeValueRepo.findCodeValueByCodeType_NameAndName(type, value)
        if (retrieved != null) {
            return retrieved
        }

        var codeTypeEntity = codeTypeRepo.findCodeTypeByName(type)
        if (codeTypeEntity == null) {
            codeTypeEntity = CodeType(name = type)
        }
        val codeValueEntity = CodeValue(codeType = codeTypeEntity, name = value)
        codeTypeEntity.codeValues.add(element = codeValueEntity)
        codeTypeRepo.save(codeTypeEntity)
        return codeValueEntity
    }

    @Transactional
    fun idempotent(type: CodeTypeEnum, values: List<CodeValueEnum>): List<CodeValue>? {
        val retrieved = codeValueRepo.findCodeValuesByCodeType_NameAndNameIn(type, values.toList())
        if (retrieved != null) {
            val remapped: List<CodeValueEnum> = retrieved.stream().map { it.name }.collect(Collectors.toList())
            val subset: List<CodeValueEnum> = values.minus(remapped)
            if (subset.isEmpty()) {
                return retrieved
            }
            var codeTypeRetrieved = codeTypeRepo.findCodeTypeByName(type)
            if (codeTypeRetrieved != null) {
                for (s in subset) {
                    codeTypeRetrieved.codeValues.add(CodeValue(codeType = codeTypeRetrieved, name = s))
                }
                codeTypeRepo.save(codeTypeRetrieved)

                return codeTypeRetrieved.codeValues.toList()
            }

            // If codeType doesn't exist
            codeTypeRetrieved = CodeType(name = type)
            for(x in subset) {
                codeTypeRetrieved.codeValues.add(CodeValue(codeType = codeTypeRetrieved, name = x))
            }
            codeTypeRepo.save(codeTypeRetrieved)
            return codeTypeRetrieved.codeValues

        }
        return null
    }
}

enum class CodeTypeEnum {
    CATEGORY, STATUS, TRANSACTION_TYPE
}

enum class CodeValueEnum(
    val value_name: String
) {
    CATEGORY_PAYNOW("PayNow"),
    CATEGORY_ATM("ATM"),
    STATUS_SUCCESS("Success"),
    STATUS_PENDING("Pending"),
    STATUS_FAIL("Fail"),

}


