package com.learn.bookstore_backend.database.model

import java.time.LocalDateTime
import javax.persistence.*


@Entity
class UserEntity(
    @Column(unique = true) var username: String,
    var activated: Boolean = true,
) : BaseEntity()

@Entity @Table(name = "tbl_item")
class ItemEntity(
    @Column(unique = true, length = 100)
    var itemName: String,
    var itemType: ItemTypeEnum
) : BaseEntity()

@Entity @Table(name = "tbl_store")
class StoreEntity(
    var storeName: String,
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "store")
    var items:List<StoreItemEntity> = ArrayList()
) : BaseEntity()

@Entity @Table(name = "tbl_store_items")
class StoreItemEntity(
    @Column(length = 10) var itemQuantity: Int = 0,
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "item_id") var item: ItemEntity,
    @ManyToOne(fetch = FetchType.LAZY) var store: StoreEntity
) : BaseEntity()

enum class ItemTypeEnum {
    BOOKS, STATIONARY, APPAREL
}
