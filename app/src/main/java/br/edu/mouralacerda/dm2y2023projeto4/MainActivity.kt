package br.edu.mouralacerda.dm2y2023projeto4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.edu.mouralacerda.dm2y2023projeto4.databinding.ActivityMainBinding
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private val b by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(b.root)

        b.btnSalvar.setOnClickListener {

            salvarDadosNaPlanilha()

        }

    }


    private fun salvarDadosNaPlanilha() {
        val nomeD = b.edtNome.text.toString().trim()
        val codigoD = b.edtCodigo.text.toString().trim()

        // O endereço do script é aquele fornecido na implantação
        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST,
            "https://script.google.com/macros/s/AKfycbx2Rz9f0nemuWxlv-uims9fG6R84totMYVjaS9w5DM4lNxT2UJEPPBcd0JGfgVtFQ4t/exec",
            object : Response.Listener<String> {
                override fun onResponse(response: String) {
                    if (response == "sucesso") {
                        Toast.makeText(this@MainActivity, "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Erro ao salvar dados!", Toast.LENGTH_SHORT).show()

                        Log.e("REPOSTA", response);
                    }
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.e("REPOSTA", error?.message.toString());
                }
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params.put("action", "adicionar");
                params.put("nome", nomeD);
                params.put("codigo", codigoD);
                return params
            }
        }

        val socketTimeOut = 50000
        val retryPolicy: RetryPolicy = DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.setRetryPolicy(retryPolicy)
        val queue: RequestQueue = Volley.newRequestQueue(this)
        queue.add(stringRequest)
    }


}