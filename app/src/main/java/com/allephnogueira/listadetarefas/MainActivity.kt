package com.allephnogueira.listadetarefas

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.allephnogueira.listadetarefas.adapter.TarefaAdapter
import com.allephnogueira.listadetarefas.database.TarefaDAO
import com.allephnogueira.listadetarefas.databinding.ActivityMainBinding
import com.allephnogueira.listadetarefas.model.Tarefa

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var listaDeTarefas = emptyList<Tarefa>() // Criando uma lista vazia
    private var tarefaAdapter : TarefaAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /** Mudando a cor da barra de status */
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.primaria, theme)


        /** Abrindo o layout quando o usuario clicar no botao + */
        binding.fabAdicionar.setOnClickListener {
            val intent = Intent(this, AdicionarTarefaActivity::class.java)
            startActivity(intent)
        }





        // Construir o recyclerView
        tarefaAdapter = TarefaAdapter(
            /** Aqui estamos passando a função que vamos criar, passando o ID do item que queremos remover
             *
             */

            { id -> confirmarExclusao(id) },
            /** Aqui vamos fazer a funçao de editar uma tarefa, dentro do metodo que vamos fazer os detalhes */
            {tarefa -> editar(tarefa)}        /** Abrindo o layout para quando o usuario clicar em atualizar */
        )

        binding.rvTarefas.adapter = tarefaAdapter

        binding.rvTarefas.layoutManager = LinearLayoutManager(this)


    }

    private fun editar(tarefa: Tarefa) {
        val intent = Intent(this, AdicionarTarefaActivity::class.java)
        /** Aqui vamos pegar essa tarefa e passar pra la
         * Aqui vamos colocar na CHAVE o nome Tarefa e vamos passar a tarefa pra la. */
        intent.putExtra("tarefa", tarefa)

        startActivity(intent)
    }

    private fun confirmarExclusao(id: Int) {
        /** Aqui dentro vamos criar um Alert Dialog para a confirmação
         *
         */

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Confirmar exclusão?")
        alertDialog.setMessage("Deseja realmente excluir a tarefa?")

        /**Aqui temos 2 valores que não vamos utilizar, então para eles nao ficar ocupando espaço na memoria colocamos o _
         *  Aqui estamos definindo um texto e a funçao lambda para quando o usuario clicar no sim */

        alertDialog.setPositiveButton("Sim") { _, _ ->

            val tarefaDAO = TarefaDAO(this)
            tarefaDAO.remover(id) // Aqui vamos passar o id da tarefa que recebemos como parametro.

            if (tarefaDAO.remover( id )) {
                /** Lembrar que o tarefa remover vai retornar um verdadeiro ou falso
                 * Se retornar verdadeiro caimos nesse toast aqui
                 * Se nao caimos no ELSE
                 */
                Toast.makeText(this, "Tarefa removida", Toast.LENGTH_SHORT).show()
                atualizarListaTarefas() // Atualizando o RecyclerView para recarregar lista de tarefas

            }else {
                Toast.makeText(this, "Falha ao remover", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.setNegativeButton("Não") { _, _ ->}

        alertDialog
            .create()
            .show()

    }

    private fun atualizarListaTarefas(){
        val tarefaDAO = TarefaDAO(this)
        listaDeTarefas = tarefaDAO.listarTarefa()
        tarefaAdapter?.adicionarLista(listaDeTarefas)
    }

    override fun onStart() {
        /** Lembra que o onCreate ele executa apenas uma vez
         * Então criamos o onStart que sempre quando o usuario sai e volta ele executa novamente, assim sempre atualizando a lista de tarefas
         * Para relembrar o onStart ele é chamado toda vez que o usuario sai da tela
         * onStart é o metodo que geralmente a gente usa para recuperar dados.
         */
        super.onStart()
        // Toda vez que a gente sair e entrar na pagina vamos chamar esse metodo passando os novos dados e o RecyclerView vai atualizar.
        atualizarListaTarefas()



    }
}