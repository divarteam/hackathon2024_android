package ru.divarteam.franimal.presentation.shop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import ru.divarteam.franimal.databinding.FragmentShopBinding

@AndroidEntryPoint
class ShopFragment : Fragment() {

    private lateinit var binding: FragmentShopBinding
    private val shopViewModel: ShopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentShopBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        shopViewModel.productsList.observe(viewLifecycleOwner) {
            binding.productsRecycler.withModels {
                it.forEach {
                    product {
                        id(it.intId)
                        productResponse(it)
                        buyProduct {
                            if (it.intId != null)
                                shopViewModel.buyProduct(it.intId) {
                                    onError(it)
                                }
                        }
                    }
                }
            }
        }

        shopViewModel.loadProductsList {
            onError(it)
        }
    }

    fun onError(errorText: String) {
        Toast.makeText(
            context,
            "Error: $errorText",
            Toast.LENGTH_SHORT
        ).show()
    }

}