package com.example.bettercalculator.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.kaen.dagger.BadSyntaxException
import io.kaen.dagger.ExpressionParser
import java.lang.IllegalArgumentException
import java.lang.Math.pow
import java.lang.NumberFormatException
import java.lang.reflect.InvocationTargetException
import java.math.BigDecimal
import kotlin.math.*

class CalculatorViewModel : ViewModel() {
    private var _expression = MutableLiveData<String>()
    val expression: LiveData<String>
        get() = _expression

    private var _displayValue = MutableLiveData<String>()
    val displayValue: LiveData<String>
        get() = _displayValue

    private var _value = MutableLiveData<String>()
    val value: LiveData<String>
        get() = _value

    private var _operatorButtonClicked = MutableLiveData<Boolean>()
    val operatorButtonClicked: LiveData<Boolean>
        get() = _operatorButtonClicked

    private var _numberButtonClicked = MutableLiveData<Boolean>()

    private var _decimalButtonClicked = MutableLiveData<Boolean>()
    val decimalButtonClicked: LiveData<Boolean>
        get() = _decimalButtonClicked

    private var _equalsClicked = MutableLiveData<Boolean>()
    val equalsClicked: LiveData<Boolean>
        get() = _equalsClicked

    private var _rightParenClicked = MutableLiveData<Boolean>()

    private var _rootClicked = MutableLiveData<Boolean>()

    private var _secondClicked = MutableLiveData<Boolean>()
    val secondClicked: LiveData<Boolean>
        get() = _secondClicked

    private var _leftRightDiff = MutableLiveData<Int>()

    private val parser = ExpressionParser()

    private val precision = 8

    init {
        _secondClicked.value = false
        initialize()
    }

    private fun initialize() {
        _expression.value = ""
        _displayValue.value = ""
        _value.value = ""
        _operatorButtonClicked.value = false
        _equalsClicked.value = false
        _numberButtonClicked.value = false
        _rootClicked.value = false
        _rightParenClicked.value = false
        _leftRightDiff.value = 0
    }

    private fun addToExpression(toAdd: String) {
        _expression.value = "${_expression.value}$toAdd"
    }

    private fun isNumber(s: String?): Boolean {
        return if (s.isNullOrEmpty()) false else s.all { Character.isDigit(it) || it == '.' || it == '-' || it == 'E'}
    }

    fun onNumberButtonClicked(rep: Int) {
        _numberButtonClicked.value = true

        if (_equalsClicked.value!!) {
            initialize()
        }

        when {
            _operatorButtonClicked.value!! || _rightParenClicked.value!!-> {
                _operatorButtonClicked.value = false
                _rightParenClicked.value = false
                _value.value = "$rep"
                _displayValue.value = "$rep"
            }

            _value.value == "" -> {
                _displayValue.value = "$rep"
                _value.value = "$rep"

            }

            else -> {
                _displayValue.value = "${_displayValue.value}$rep"
                _value.value = "${_value.value}$rep"

            }
        }
    }

    fun onDoubleButtonClicked(rep: Double) {
        if (_expression.value?.isEmpty()!!) {
            initialize()
        }

        if (_operatorButtonClicked.value!!) {
            _operatorButtonClicked.value = false
        }

        val expr = parser.evaluate("$rep", precision)

        _value.value = "$rep"
        _displayValue.value = expr.toString()
    }

    fun onOperatorButtonClicked(rep: String) {
        var toAdd = ""
        if (_equalsClicked.value!!) {
            _equalsClicked.value = false
        }

        if (_numberButtonClicked.value!!) {
            _numberButtonClicked.value = false
        }

        if (_rootClicked.value!! && _leftRightDiff.value!! > 0) {
            toAdd = ")"
            _rootClicked.value = false
        }

        addToExpression("${_value.value}$toAdd$rep")

        _operatorButtonClicked.value = true
    }

    fun onEqualsButtonClicked() {
        try {
            if (isNumber(_value.value)) {
                addToExpression("${_value.value}")
            }

            _expression.value = _expression.value?.replace("--", "+")
            val expr = parser.evaluate(_expression.value!!, precision)

            if (expr.isNaN() || expr.isInfinite()) {
                onError()
            } else {
                _displayValue.value = expr.toString()
                _value.value = expr.toString()
                _expression.value = ""
                _operatorButtonClicked.value = false
                _equalsClicked.value = true
                _numberButtonClicked.value = false
                _rootClicked.value = false
                _leftRightDiff.value = 0
            }
        } catch (e: NumberFormatException) {
            onError()
        }
    }

    fun onClearButtonClicked() {
        initialize()
    }

    fun onDecimalButtonClicked() {
        _displayValue.value = "${_displayValue.value}."
        _value.value = "${_value.value}."
    }


    fun onRandomButtonClicked() {
        onDoubleButtonClicked(Math.random())
    }

    fun onLeftParenButtonClicked() {
        _leftRightDiff.value = _leftRightDiff.value?.plus(1)
        addToExpression("(")
        _value.value = ""
    }

    fun onRightParenButtonClicked() {
        if (_leftRightDiff.value!! > 0) {
            _leftRightDiff.value = _leftRightDiff.value?.minus(1)
            val regex = Regex("""\(([^()]*)\)""")

            if (isNumber(_value.value)) addToExpression("${_value.value})")
            else addToExpression(")")

            val matches = regex.findAll(_expression.value!!).toList()
            val lastMatch = matches[matches.lastIndex].value
            val replacement = parser.evaluate(lastMatch, precision).toString()

            _value.value = replacement
            _displayValue.value = replacement

            _expression.value = _expression.value!!.replace(lastMatch, "")

        }
    }

    private fun onMathButtonClicked(precision: Int = 10, function: (input: Double) -> Double) {
        val isNumber = isNumber(_value.value)
        val regex = Regex("""\(([^()]*)\)""")

        if (isNumber && !_operatorButtonClicked.value!!) {
            try {
                var expr = function(_value.value!!.toDouble())

                if (expr.isNaN() || expr.isInfinite()) {
                    onError()
                } else {
                    expr = parser.evaluate("$expr", precision)
                    _displayValue.value = expr.toString()
                    _value.value = expr.toString()
                    _expression.value = regex.replace(_expression.value!!, _value.value!!)
                    _equalsClicked.value = true
                }
            } catch (e: IllegalArgumentException) {
                onError()
            }

        } else {
            onError()
        }
    }

    fun onFactorialButtonClicked() {
        fun factorial(x: Double): Double {
            return if (x < 0 || x > 170) {
                throw IllegalArgumentException()
            }
            else if (x == 0.0 || x == 1.0) {
                1.0
            } else {
                x * factorial(x - 1)
            }
        }
        onMathButtonClicked {
            factorial(it)
        }
    }

    fun onPercentButtonClicked() {
        onMathButtonClicked { it / 100 }
    }

    fun onChangeSignButtonClicked() {
        onMathButtonClicked { -it }
    }

    fun onNaturalLogButtonClicked() {
        fun log(x: Double): Double {
            if (x <= 0) throw IllegalArgumentException()
            else return ln(x)
        }
        onMathButtonClicked { log(it) }
    }

    fun onLogButtonClicked() {
        fun log(x: Double): Double {
            if (x <= 0) throw IllegalArgumentException()
            else return log10(x)
        }
        onMathButtonClicked { log(it) }
    }

    fun onSinButtonClicked() {
        onMathButtonClicked { sin(it) }
    }

    fun onCosButtonClicked() {
        onMathButtonClicked { cos(it) }
    }

    fun onTanButtonClicked() {
        onMathButtonClicked { tan(it) }
    }

    fun onSinHButtonClicked() {
        onMathButtonClicked { sinh(it) }
    }

    fun onCosHButtonClicked() {
        onMathButtonClicked { cosh(it) }
    }

    fun onTanHButtonClicked() {
        onMathButtonClicked { tanh(it) }
    }

    fun onReciprocalButtonClicked() {
        fun recip(x: Double): Double {
            if (x == 0.0) throw IllegalArgumentException()
            else return 1/x
        }
        onMathButtonClicked (20){ recip(it) }
    }

    fun onSquaredButtonClicked() {
        onMathButtonClicked { it.pow(2) }
    }

    fun onEToTheXButtonClicked() {
        onMathButtonClicked { Math.E.pow(it) }
    }

    fun on10ToTheXButtonClicked() {
        onMathButtonClicked { 10.0.pow(it) }
    }

    fun onRoundButtonClicked() {
        onMathButtonClicked { round(it) }
    }

    fun onSquareRootButtonClicked() {
        fun squareR(x: Double): Double {
            if (x < 0) throw IllegalArgumentException()
            else return sqrt(x)
        }
        onMathButtonClicked { squareR(it) }
    }

    fun onRootButtonClicked() {
        _value.value = "(${_value.value})"
        onOperatorButtonClicked("^(1/")
        _rootClicked.value = true
        _leftRightDiff.value = _leftRightDiff.value?.plus(1)
    }

    fun onSecondClicked() {
        _secondClicked.value = !_secondClicked.value!!
    }

    fun onArcSinButtonClicked() {
        onMathButtonClicked { asin(it) }
    }

    fun onArcCosButtonClicked() {
        onMathButtonClicked { acos(it) }
    }

    fun onArcTanButtonClicked() {
        onMathButtonClicked { atan(it) }
    }

    fun onArcSinHButtonClicked() {
        onMathButtonClicked { asinh(it) }
    }

    fun onArcCosHButtonClicked() {
        onMathButtonClicked { acosh(it) }
    }

    fun onArcTanHButtonClicked() {
        onMathButtonClicked { atanh(it) }
    }

    fun onLogBase2ButtonClicked() {
        fun log(x: Double): Double {
            if (x <= 0) throw IllegalArgumentException()
            else return log2(x)
        }
        onMathButtonClicked { log(it) }
    }

    fun on2ToTheXButtonClicked() {
        onMathButtonClicked { 2.0.pow(it) }
    }

    private fun onError() {
        _displayValue.value = "Error"
        _value.value = ""
        _expression.value = ""
        _operatorButtonClicked.value = false
        _equalsClicked.value = true
        _numberButtonClicked.value = false
        _rootClicked.value = false
        _leftRightDiff.value = 0
    }



}
