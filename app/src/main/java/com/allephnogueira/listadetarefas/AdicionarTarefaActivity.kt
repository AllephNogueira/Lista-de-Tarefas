package com.allephnogueira.listadetarefas

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.allephnogueira.listadetarefas.database.TarefaDAO
import com.allephnogueira.listadetarefas.databinding.ActivityAdicionarTarefaBinding
import com.allephnogueira.listadetarefas.model.Tarefa

class AdicionarTarefaActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAdicionarTarefaBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // Tarefa inicia como null, se for null quer dizer que estamos salvando
        // Se receber algo dentro dela quer dizer que estamos alterando.
        var tarefa: Tarefa? = null

        // Recuperar tarefa que foi passada
        val bundle = intent.extras
        // Vamos fazer um teste para saber se a pessoa esta editando a tarefa ou adicionando uma nova tarefa
        if (bundle!=null) {
            tarefa = bundle.getSerializable("tarefa") as Tarefa // as Tarefa para converter
            binding.editTarefa.setText(tarefa.descricao)
        }


        binding.btnSalvar.setOnClickListener {

            // Primeiro vamos testar se objeto esta vazio
            if (binding.editTarefa.text.isNotEmpty()) {
                // Agora antes de salvar, vamos verificar se o usuario quer salvar ou atualizar
                // Lembra que se a tarefa que fizemos la em cima for null quer dizer que estamos salvando
                // Se tiver algo dentro dela, quer dizer que estamos editando

                if (tarefa != null) {
                    editar( tarefa )
                }else {
                    salvar()
                }

            }else {
                Toast.makeText(this,
                    "Preencha uma tarefa",
                    Toast.LENGTH_SHORT)
                    .show()
            }




        }

    }

    private fun editar(tarefa: Tarefa) {

        val tarefaRecuperada = binding.editTarefa.text.toString()
        // Agora vamos criar uma nova tarefa do 0
        val tarefaAtualizar = Tarefa(
            tarefa.idTarefa,
            tarefaRecuperada,
            "DEFAULT"
        )
        // Precismaos pegar o idTarefa para fazer atualização nesse dado.

        val tarefaDAO = TarefaDAO(this)

        // TarefaDAO retorna sempre um verdadeiro ou falso
        // Aqui estamos verificando se a tarefa foi atualizada mesmo.
        if (tarefaDAO.atualizar(tarefaAtualizar)) {
            // Se for verdadeiro salvamos a tarefa e exibimos o toast e fechando a actvitiy
            Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_LONG).show()
            finish()
        } else {
            // Se nao salvar exibimos um texto
            Toast.makeText(this, "Falha ao atualizar tarefa", Toast.LENGTH_SHORT).show()
        }

    }

    private fun salvar() {
        // Caso nao esteja vazio vamos recuperar o texto dentro dele
        val descricaoTarefa = binding.editTarefa.text.toString()
        // Vamos criar um novo objeto de tarefa passando os dados como ID, DESCRICAO e DATA
        // Lembrando que descricao é oque o USUARIO digitou
        val tarefa = Tarefa(
            -1,
            descricaoTarefa,
            "DEFAULT"
        )

        // Vamos iniciar o TAREFADAO passando o contexto de onde vai ser executado
        // Vamos instanciar o TAREFADAO para a gente poder pegar os metodos que estao la dentro, como salvar.
        val tarefaDAO = TarefaDAO(this)


        // TarefaDAO retorna sempre um verdadeiro ou falso
        if (tarefaDAO.salvar(tarefa)) {
            // Se for verdadeiro salvamos a tarefa e exibimos o toast e fechando a actvitiy
            Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_LONG).show()
            finish()
        } else {
            // Se nao salvar exibimos um texto
            Toast.makeText(this, "Falha ao salvar tarefa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun salvarTarefa(nomeTarefa: String) {

    }
}