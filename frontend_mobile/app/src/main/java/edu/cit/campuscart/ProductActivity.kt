package edu.cit.campuscart
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.cit.campuscart.models.Products
import edu.cit.campuscart.adapters.ProductAdapters
import edu.cit.campuscart.fragments.ProductDetailDialogFragment
import edu.cit.campuscart.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browsepage)

        recyclerView = findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPref = getSharedPreferences("CampusCartPrefs", MODE_PRIVATE)
        val loggedInUser = sharedPref.getString("loggedInUsername", "") ?: ""

        fetchProductData(loggedInUser)
    }

    private fun fetchProductData(loggedInUser: String) {
        val apiService = RetrofitClient.instance
        val call = apiService.getAllProducts(loggedInUser)

        call.enqueue(object : Callback<List<Products>> {
            override fun onResponse(
                call: Call<List<Products>>,
                response: Response<List<Products>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val allProducts = response.body()!!
                    val approvedProducts = allProducts.filter {
                        it.status.equals("approved", ignoreCase = true) &&
                                !it.userUsername.isNullOrBlank()
                    }

                    productAdapter = ProductAdapters(approvedProducts.toMutableList()) { selectedProduct ->
                        val dialog = ProductDetailDialogFragment.newInstance(selectedProduct)
                        dialog.show(supportFragmentManager, "ProductDetailDialog")
                    }

                    recyclerView.adapter = productAdapter
                } else {
                    Toast.makeText(
                        this@ProductActivity,
                        "Failed to load products",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                Toast.makeText(
                    this@ProductActivity,
                    "Network error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}