package ru.gustavo.binlistapp.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gustavo.binlistapp.R
import ru.gustavo.binlistapp.databinding.InfoFragmentBinding
import ru.gustavo.binlistapp.db.BankInfo
import ru.gustavo.binlistapp.db.CardInfo
import ru.gustavo.binlistapp.db.CardInfoDao
import ru.gustavo.binlistapp.db.MainDb
import ru.gustavo.binlistapp.entities.Card
import ru.gustavo.binlistapp.network.BinApi
import ru.gustavo.binlistapp.network.RetrofitClient
import ru.gustavo.binlistapp.util.StringArg
import ru.gustavo.binlistapp.util.countryCodeToUnicodeFlag
import kotlin.concurrent.thread

class InfoFragment : Fragment() {
    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val retrofit: BinApi by lazy { RetrofitClient.getApiClient() }
    private lateinit var db: CardInfoDao
    private lateinit var binding: InfoFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = InfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getInfo()
        binding.phoneTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${binding.phoneTextView.text}")
            startActivity(intent)
        }
        binding.coordinateTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:${binding.geoTextView.text}")
            startActivity(intent)
        }
    }

    private fun getInfo() {
        val value = arguments?.textArg
        db = MainDb.getDb(requireContext()).cardInfoDao()
        if (value != null) {
            retrofit.getBinInfo(bin = value).enqueue(object : Callback<Card> {
                override fun onResponse(
                    call: Call<Card>, response: Response<Card>
                ) {
                    if (response.isSuccessful) {
                        val bankInfo = BankInfo(
                            response.body()?.bank?.name.toString(),
                            response.body()?.bank?.url.toString(),
                            response.body()?.bank?.phone.toString(),
                            response.body()?.bank?.city.toString()
                        )
                        val bankCard = CardInfo(
                            null,
                            value,
                            response.body()?.scheme.toString(),
                            response.body()?.type.toString(),
                            response.body()?.brand.toString(),
                            response.body()?.country?.name.toString(),
                            bankInfo
                        )
                        thread {
                            db.insert(bankCard)
                        }
                        binding.apply {
                            val name = response.body()?.bank?.name ?: ""
                            val city = response.body()?.bank?.city ?: ""
                            val country =
                                "${response.body()?.country?.alpha2?.countryCodeToUnicodeFlag() ?: ""} ${response.body()?.country?.name ?: ""}"
                            val latitude = "${response.body()?.country?.latitude}"
                            val longitude = "${response.body()?.country?.longitude}"
                            val geo = "$latitude,$longitude"
                            val coordinates = getString(R.string.coordinates, latitude, longitude)
                            bankNameTextView.text =
                                if (city.isBlank()) name else if (name.isBlank()) city else "$name, $city"
                            siteTextView.text = response.body()?.bank?.url
                            phoneTextView.text = response.body()?.bank?.phone
                            schemeTextView.text = response.body()?.scheme
                            brandTextView.text = response.body()?.brand
                            countryTextView.text = country
                            coordinateTextView.text = coordinates
                            typeTextView.text = response.body()?.type
                            prepaidTextView.text =
                                if (response.body()?.prepaid == true) "Yes" else "No"
                            lengthTextView.text = response.body()?.number?.length.toString()
                            luhnTextView.text =
                                if (response.body()?.number?.luhn == true) "Yes" else "No"
                            geoTextView.text = geo
                        }
                    }
                }

                override fun onFailure(call: Call<Card>, t: Throwable) {
                    Log.d("MainActivity", "Error")
                }
            })
        }
    }
}