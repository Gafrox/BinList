package ru.gustavo.binlistapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import ru.gustavo.binlistapp.R
import ru.gustavo.binlistapp.databinding.InputFragmentBinding
import ru.gustavo.binlistapp.db.CardInfoDao
import ru.gustavo.binlistapp.db.MainDb
import ru.gustavo.binlistapp.ui.InfoFragment.Companion.textArg
import ru.gustavo.binlistapp.util.AndroidUtils
import kotlin.concurrent.thread

class InputFragment : Fragment() {

    private lateinit var db: CardInfoDao
    private lateinit var binding: InputFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = MainDb.getDb(requireContext()).cardInfoDao()
        var request = ""
        db.getAll().asLiveData().observe(this.viewLifecycleOwner) { list ->
            list.forEach {
                val text =
                    "BIN: ${it.bin}" +
                            " BRAND: ${it.brand}," +
                            " COUNTRY: ${it.countryName}," +
                            " BANK: ${it.bankInfo.bankName}\n\n"
                request = if (request.isEmpty()) text else text + request
                binding.historyList.text = request

            }
        }
        binding.getButton.setOnClickListener {
            if (binding.BinEditText.text.isNullOrBlank()) {
                val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
                binding.BinEditText.startAnimation(shake)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_message),
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {
                binding.BinEditText.text.toString().let {
                    val bundle = Bundle().apply {
                        textArg = it
                    }
                    val action = R.id.action_inputFragment_to_infoFragment
                    findNavController().navigate(action, bundle)
                    AndroidUtils.hideKeyboard(requireView())
                }
            }
        }
        binding.deleteHistoryFab.setOnClickListener {
            thread {
                db.deleteAll()
            }
            request = ""
            binding.historyList.text = ""
        }
    }
}