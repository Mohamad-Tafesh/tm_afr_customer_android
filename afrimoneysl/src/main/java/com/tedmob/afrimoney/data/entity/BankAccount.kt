package com.tedmob.afrimoney.data.entity

class BankAccount(
    val id: BankAccountId,
    val name: String,
) {
    override fun toString(): String = name
}

@JvmInline
value class BankAccountId(val value: String)