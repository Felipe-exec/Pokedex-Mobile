package br.com.fabricadesinapse.pokedex_android.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import br.com.fabricadesinapse.pokedex_android.R
import br.com.fabricadesinapse.pokedex_android.domain.PokemonDetail
import com.squareup.picasso.Picasso
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


//PRECISO DESMEMBRAR ESSA CLASSE EM PEDAÇOS PARA FAZER SENTIDO COM O PROJETO!

//Vou colocar comentários abaixo para saber o que deve ser feito. Sinta-se livre para colaborar.
class DetailActivity : AppCompatActivity() {

    private val buttonVoltar by lazy {
        findViewById<Button>(R.id.btnVoltar)
    }

    private val buttonSalvar by lazy {
        findViewById<ImageButton>(R.id.salvarPokemon)
    }

    //Não era pra isso estar aqui----------------------
    private lateinit var tvNameDetail: TextView
    private lateinit var tvWeightDetail: TextView
    private lateinit var tvHeightDetail: TextView
    private lateinit var tvType1Detail: TextView
    private lateinit var tvType2Detail: TextView
    private lateinit var tvHPDetail: TextView
    private lateinit var tvAttackDetail: TextView
    private lateinit var tvDefenseDetail: TextView
    private lateinit var tvSpAtkDetail: TextView
    private lateinit var tvSpDefDetail: TextView
    private lateinit var tvSpeedDetail: TextView
    private lateinit var ivPokemonDetail: ImageView
    private lateinit var apiService: PokemonApiService
    //---------------------------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //botão para voltar!!
        buttonVoltar.setOnClickListener {
            finish()
        }

        //botão para salvar!
        buttonSalvar.setOnClickListener {
            buttonSalvar.setImageResource(R.drawable.pokeball)
            Toast.makeText(this, "Botão de salvar em desenvolvimento!", Toast.LENGTH_SHORT).show()
        }

        //Não era pra isso estar aqui--------------------------------------------
        tvNameDetail = findViewById(R.id.tvNameDetail)
        tvWeightDetail = findViewById(R.id.tvWeightDetail)
        tvHeightDetail = findViewById(R.id.tvHeightDetail)
        tvType1Detail = findViewById(R.id.tvType1Detail)
        tvType2Detail = findViewById(R.id.tvType2Detail)
        tvHPDetail = findViewById(R.id.tvHPDetail)
        tvAttackDetail = findViewById(R.id.tvAttackDetail)
        tvDefenseDetail = findViewById(R.id.tvDefenseDetail)
        tvSpAtkDetail = findViewById(R.id.tvSpAtkDetail)
        tvSpDefDetail = findViewById(R.id.tvSpDefDetail)
        tvSpeedDetail = findViewById(R.id.tvSpeedDetail)
        ivPokemonDetail = findViewById(R.id.ivPokemonDetail)
        //-------------------------------------------------------------------------

        // Configurar o Retrofit ? (já temos um retrofit em *PokemonRepository*)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //-------------------------------------------------------------------------


        // Criar a instância do serviço da API ? (temos a classe *PokemonService* justamente para isso)
        apiService = retrofit.create(PokemonApiService::class.java)
        //-------------------------------------------------------------------------



        // Obtém o ID do Pokémon selecionado da intent (talvez em *PokemonAdapter*?)
        val pokemonId = intent.getIntExtra("pokemonNumber", 1)
        //-------------------------------------------------------------------------



        // Chama o método para obter os detalhes do Pokémon (talvez em *PokemonAdapter*?)
        getPokemonDetails(pokemonId)
        //-------------------------------------------------------------------------
    }


    //Okay, isso definitivamente não era para estar aqui (acho que caberia em PokemonRepository!)
    private fun getPokemonDetails(pokemonId: Int) {
        apiService.getPokemonDetails(pokemonId).enqueue(object : Callback<PokemonDetail> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {
                if (response.isSuccessful) {
                    val pokemonDetails = response.body()
                    if (pokemonDetails != null) {
                        try {
                            val name = pokemonDetails.name
                            val weight = pokemonDetails.weight
                            val weightDividedBy10 = weight / 10
                            val height = pokemonDetails.height
                            val heightDividedBy10 = height / 10
                            val types = pokemonDetails.types
                            val type1Name = types[0].type.name
                            val type2Name = if (types.size > 1) types[1].type.name else null
                            val stats = pokemonDetails.stats
                            val hp = stats[0].baseStat
                            val attack = stats[1].baseStat
                            val defense = stats[2].baseStat
                            val spAtk = stats[3].baseStat
                            val spDef = stats[4].baseStat
                            val speed = stats[5].baseStat
                            val spriteUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemonId}.png"

                            // Atualiza os elementos da UI com os dados do Pokémon
                            tvNameDetail.text = name
                            tvWeightDetail.text = "Weight: $weightDividedBy10 kg"
                            tvHeightDetail.text = "Height: $heightDividedBy10 m"
                            tvType1Detail.text = type1Name
                            tvType2Detail.text = type2Name
                            tvHPDetail.text = "HP: $hp"
                            tvAttackDetail.text = "Attack: $attack"
                            tvDefenseDetail.text = "Defense: $defense"
                            tvSpAtkDetail.text = "Sp. Attack: $spAtk"
                            tvSpDefDetail.text = "Sp. Defense: $spDef"
                            tvSpeedDetail.text = "Speed: $speed"

                            // Carrega a imagem do Pokémon usando a biblioteca Picasso
                            Picasso.get().load(spriteUrl).into(ivPokemonDetail)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
    //-----------------------------------------------------------------------------------------------


    // Define a interface do serviço da API (Isso é papel de PokemonService)
    interface PokemonApiService {
        @GET("pokemon/{id}/")
        fun getPokemonDetails(@Path("id") id: Int): Call<PokemonDetail>
    }
    //----------------------------------------------------------------------
}
