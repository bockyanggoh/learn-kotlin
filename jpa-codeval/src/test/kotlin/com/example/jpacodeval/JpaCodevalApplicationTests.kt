package com.example.jpacodeval

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.util.AssertionErrors.assertEquals
import java.lang.AssertionError
import java.lang.Exception
import javax.transaction.Transactional

@SpringBootTest
@ActiveProfiles("test")
class JpaCodevalApplicationTests(
    @Autowired val codeValueDatabaseService: CodeValueDatabaseService,
    @Autowired val codeValueDb: CodeValueRepo,
    @Autowired val codeTypeDb: CodeTypeRepo
) {

    @Test
    @Transactional
    internal fun TestCodeValueInsert() {
        codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, CodeValueEnum.CATEGORY_ATM)
        codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, CodeValueEnum.CATEGORY_PAYNOW)
        val x = codeTypeDb.findCodeTypeByName(name = CodeTypeEnum.CATEGORY)
        if (x != null) {
            print(x.name)
            print(x.id)
            for( x2 in x.codeValues!!) {
                println("Data Id: "+ x2.id)
                println("Data Name: "+ x2.name)
            }
        }
    }

    @Test
    @Transactional
    internal fun `Given1CodeTypeAnd1CodeValueInDB, WhenInserting2CodeValueAnd0CodeType, It Should Succeed`() {
        codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, CodeValueEnum.CATEGORY_PAYNOW)
        var range = arrayListOf(CodeValueEnum.CATEGORY_ATM, CodeValueEnum.STATUS_FAIL)
        var x = codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, range)
        if (x != null) {
            for( x2 in x) {
                println("Data Id: "+ x2.id)
                println("Data Name: "+ x2.name)
            }
        }
    }


    @Test
    @Transactional
    internal fun `GivenNoCodeTypeAndCodeValueInDB, WhenInserting3CodeValueAnd1CodeType, It Should Succeed`() {
        val range = arrayListOf(CodeValueEnum.CATEGORY_ATM, CodeValueEnum.STATUS_FAIL, CodeValueEnum.STATUS_PENDING)
        var x = codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, range)
        if (x != null) {
            for( x2 in x) {
                println("Data Id: "+ x2.id)
                println("Data Name: "+ x2.name)
            }
        }
        val retrieved = codeValueDb.findCodeValuesByCodeType_NameAndNameIn(CodeTypeEnum.CATEGORY, range)
        assertEquals("It should be equal", x?.size, retrieved?.size)

    }

    @Test
    internal fun `GivenNoCodeTypeAndCodeValueInDB, WhenInsertingDuplicatedCodeValueAnd1CodeType, It Should Fail`() {
        val range = arrayListOf(CodeValueEnum.CATEGORY_ATM, CodeValueEnum.STATUS_FAIL, CodeValueEnum.STATUS_FAIL)
//        codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, range)
        codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, range)
//        val retrieved = codeValueDb.findCodeValuesByCodeType_NameAndNameIn(CodeTypeEnum.CATEGORY, arrayListOf(CodeValueEnum.CATEGORY_ATM, CodeValueEnum.STATUS_FAIL))
//        assertEquals("It should be equal", retrieved?.size, 2)
    }

    @Test
    @Transactional
    internal fun `Given1CodeTypeAnd3CodeValueInDB, WhenInsertingNoNewCodeValueAndNoNewCodeType, It Should not Insert anything`() {
        val range = arrayListOf(CodeValueEnum.STATUS_SUCCESS, CodeValueEnum.STATUS_FAIL, CodeValueEnum.STATUS_PENDING)
        val x = codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, range)
        val repeated = codeValueDatabaseService.idempotent(CodeTypeEnum.CATEGORY, range)
        val retrieved = codeValueDb.findCodeValuesByCodeType_NameAndNameIn(CodeTypeEnum.CATEGORY, range)
        assertEquals("It should be equal", x?.size, retrieved?.size)
    }
}
