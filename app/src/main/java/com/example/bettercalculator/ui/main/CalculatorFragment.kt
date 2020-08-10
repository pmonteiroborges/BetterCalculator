package com.example.bettercalculator.ui.main

import android.content.res.Configuration
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.bettercalculator.R
import com.example.bettercalculator.databinding.CalculatorFragmentBinding
import java.math.BigDecimal

class CalculatorFragment : Fragment() {
    private lateinit var binding: CalculatorFragmentBinding
    private lateinit var viewModel: CalculatorViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.calculator_fragment, container, false)
        viewModel = ViewModelProviders.of(this).get(CalculatorViewModel::class.java)
        binding.viewModel = viewModel

        viewModel.displayValue.observe(viewLifecycleOwner, Observer {
            binding.displayValue.text = it
        })

//        viewModel.value.observe(viewLifecycleOwner, Observer {
//            binding.displayValue.text = it
//        })

//        viewModel.expression.observe(viewLifecycleOwner, Observer {
//            binding.displayValue.text = it
//        })

//        viewModel.operatorButtonClicked.observe(viewLifecycleOwner, Observer {
//            binding.displayValue.text = it.toString()
//        })

        viewModel.secondClicked.observe(viewLifecycleOwner, Observer {
            if (it!!) {
                binding.buttonArcsin?.visibility = View.VISIBLE
                binding.buttonArccos?.visibility = View.VISIBLE
                binding.buttonArctan?.visibility = View.VISIBLE
                binding.buttonArcsinh?.visibility = View.VISIBLE
                binding.buttonArccosh?.visibility = View.VISIBLE
                binding.buttonArctanh?.visibility = View.VISIBLE
                binding.buttonLog2?.visibility = View.VISIBLE
                binding.buttonTwoToTheN?.visibility = View.VISIBLE

                binding.buttonSin?.visibility = View.INVISIBLE
                binding.buttonCos?.visibility = View.INVISIBLE
                binding.buttonTan?.visibility = View.INVISIBLE
                binding.buttonSinh?.visibility = View.INVISIBLE
                binding.buttonCosh?.visibility = View.INVISIBLE
                binding.buttonTanh?.visibility = View.INVISIBLE
                binding.buttonLog?.visibility = View.INVISIBLE
                binding.button10ToTheX?.visibility = View.INVISIBLE

                binding.button2nd?.setBackgroundColor(resources.getColor(R.color.secondarySecondButtonColor))
                binding.button2nd?.setTextColor(resources.getColor(R.color.secondaryTextColor))

            } else {
                binding.buttonArcsin?.visibility = View.INVISIBLE
                binding.buttonArccos?.visibility = View.INVISIBLE
                binding.buttonArctan?.visibility = View.INVISIBLE
                binding.buttonArcsinh?.visibility = View.INVISIBLE
                binding.buttonArccosh?.visibility = View.INVISIBLE
                binding.buttonArctanh?.visibility = View.INVISIBLE
                binding.buttonLog2?.visibility = View.INVISIBLE
                binding.buttonTwoToTheN?.visibility = View.INVISIBLE

                binding.buttonSin?.visibility = View.VISIBLE
                binding.buttonCos?.visibility = View.VISIBLE
                binding.buttonTan?.visibility = View.VISIBLE
                binding.buttonSinh?.visibility = View.VISIBLE
                binding.buttonCosh?.visibility = View.VISIBLE
                binding.buttonTanh?.visibility = View.VISIBLE
                binding.buttonLog?.visibility = View.VISIBLE
                binding.button10ToTheX?.visibility = View.VISIBLE

                binding.button2nd?.setBackgroundColor(resources.getColor(R.color.tertiaryColor))
                binding.button2nd?.setTextColor(resources.getColor(R.color.primaryTextColor))
            }
        })

        binding.buttonPlus.setOnClickListener {
            if (!viewModel.operatorButtonClicked.value!!) {
                viewModel.onOperatorButtonClicked("+")
            }
        }

        binding.buttonMinus.setOnClickListener {
            if (!viewModel.operatorButtonClicked.value!!) {
                viewModel.onOperatorButtonClicked("-")
            }
        }

        binding.buttonTimes.setOnClickListener {
            if (!viewModel.operatorButtonClicked.value!!) {
                viewModel.onOperatorButtonClicked("*")
            }
        }

        binding.buttonDivide.setOnClickListener {
            if (!viewModel.operatorButtonClicked.value!!) {
                viewModel.onOperatorButtonClicked("/")
            }
        }

        binding.buttonNToTheM?.setOnClickListener {
            if (!viewModel.operatorButtonClicked.value!!) {
                viewModel.onOperatorButtonClicked("^")
            }
        }

        binding.buttonRoot?.setOnClickListener {
            if (!viewModel.operatorButtonClicked.value!!) {
                viewModel.onRootButtonClicked()
            }
        }

        return binding.root
    }
}