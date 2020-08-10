package com.example.bettercalculator.ui.main
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import io.kaen.dagger.ExpressionParser
//import java.math.BigDecimal
//
//class CalculatorViewModel : ViewModel() {
//    private var _expression = MutableLiveData<String>()
//    val expression: LiveData<String>
//        get() = _expression
//
//    private var _value = MutableLiveData<Int>()
//    val value: LiveData<Int>
//        get() = _value
//
//    private var _operatorButtonClicked = MutableLiveData<Boolean>()
//    val operatorButtonClicked: LiveData<Boolean>
//        get() = _operatorButtonClicked
//
//    private var _equalsClicked = MutableLiveData<Boolean>()
//    val equalsClicked: LiveData<Boolean>
//        get() = _equalsClicked
//
//    private val parser = ExpressionParser()
//
//    init {
//        initialize()
//    }
//
//    fun onNumberButtonClicked(rep: Int) {
//        if (_equalsClicked.value!!) {
//            initialize()
//            _equalsClicked.value = false
//        }
//
//        if (_expression.value == "0") {
//            _expression.value = "$rep"
//        } else {
//            _expression.value = "${_expression.value}$rep"
//        }
//
//        if (_operatorButtonClicked.value!!) {
//            _operatorButtonClicked.value = false
//        }
//
//    }
//
//    fun onDoubleButtonClicked(rep: Double) {
//        if (_equalsClicked.value!!) {
//            initialize()
//            _equalsClicked.value = false
//        }
//
//        if (_expression.value == "0") {
//            _expression.value = "$rep"
//        } else {
//            _expression.value = "${_expression.value}$rep"
//        }
//
//        if (_operatorButtonClicked.value!!) {
//            _operatorButtonClicked.value = false
//        }
//
//    }
//
//    fun onOperatorButtonClicked(rep: String) {
//        if (_equalsClicked.value!!) {
//            _equalsClicked.value = false
//        }
//
//        _expression.value = "${_expression.value}$rep"
//        _operatorButtonClicked.value = true
//    }
//
//    fun onEqualsButtonClicked() {
//        val decimal = BigDecimal(parser.evaluate(_expression.value!!).toString())
//        _expression.value = decimal.stripTrailingZeros().toPlainString()
//        _operatorButtonClicked.value = false
//        _equalsClicked.value = true
//    }
//
//    fun onClearButtonClicked() {
//        initialize()
//    }
//
//    fun onChangeSignButtonClicked() {
//        val decimal = BigDecimal((-parser.evaluate(_expression.value!!)).toString())
//        _expression.value = decimal.stripTrailingZeros().toPlainString()
//    }
//
//    fun onPercentButtonClicked() {
//        _expression.value = (parser.evaluate(_expression.value!!) / 100).toString()
//    }
//
//    fun onFactorialButtonClicked() {
//        _expression.value = factorial(_expression.value!!.toInt()).toString()
//    }
//
//
//    fun factorial(x: Int): Int {
//        return if (x == 0 || x == 1) {
//            1
//        } else {
//            x * factorial(x - 1)
//        }
//    }
//
//    fun onRandomButtonClicked() {
//        _expression.value = Math.random().toString()
//    }
//
//    private fun initialize() {
//        _expression.value = "0"
//        _value.value = 0
//        _operatorButtonClicked.value = false
//        _equalsClicked.value = false
//    }
//}