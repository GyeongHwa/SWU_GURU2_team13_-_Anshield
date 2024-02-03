package com.android.a13app

class ExpenseCard(expensePerson: String, expenseMoney: String, expensePlace: String, expenseDate: String) {
    var expensePerson: String = expensePerson//결제한 사람 이름
        private set
    var expenseMoney: String = expenseMoney//결제한 금액
        private set
    var expensePlace: String = expensePlace//장소
        private set
    var expenseDate: String = expenseDate//날짜
        private set
}