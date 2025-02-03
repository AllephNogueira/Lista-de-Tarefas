package com.allephnogueira.listadetarefas.database

import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract.Data
import android.util.Log
import com.allephnogueira.listadetarefas.model.Tarefa

class TarefaDAO(context: Context) : ITarefasDAO {

    private val escrita = DatabaseHelper(context).writableDatabase // Fazer escrita (UPDATE, INSERT, DELETE)
    private val leitura = DatabaseHelper(context).readableDatabase // leitura (SELECT)


    override fun salvar(tarefa: Tarefa): Boolean {

        val valores = ContentValues()
        valores.put("${DatabaseHelper.COLUNA_DESCRICAO}", tarefa.descricao)


        try {
            escrita.insert(
                DatabaseHelper.NOME_TABELA_TAREFAS,
                null,
                valores
            )
            Log.i("info_db", "Sucesso ao salvar tarefa!")
        }catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao salvar tarefa!")
            return false
        }

        return true
    }

    override fun atualizar(tarefa: Tarefa): Boolean {


        val conteudo = ContentValues()
        conteudo.put("${DatabaseHelper.COLUNA_DESCRICAO}", tarefa.descricao) // Aqui é a nova descrição que estamos atualizando.
        val args = arrayOf(tarefa.idTarefa.toString()) // Aqui é onde vamos pegar o ID da tarefa que estamos querendo atualizar.

        try {
            escrita.update(
                /** Parametros que precisamos para atualizar uma tabela */
                DatabaseHelper.NOME_TABELA_TAREFAS,
                conteudo,
                "${DatabaseHelper.COLUNA_ID_TAREFA} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao atualizar tarefa!")
        }catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao atualizar tarefa!")
            return false
        }

        return true

    }

    override fun remover(idTarefa: Int): Boolean {



        val args = arrayOf(idTarefa.toString())

        try {
            escrita.delete(
                /** Vai remover da TABELA
                 * Onde id_tarefa for igual a ...
                 * (args é onde esta o ID ele esta recebendo do MainActivity)
                 *
                 * Agora apos remover precisamos usar o metodo para atualizar o recyclerView se tudo deu certo.
                 */
                DatabaseHelper.NOME_TABELA_TAREFAS,
                "${DatabaseHelper.COLUNA_ID_TAREFA} = ?",
                args
            )
            Log.i("info_db", "Sucesso ao remover tarefa!")
        }catch (e: Exception) {
            e.printStackTrace()
            Log.i("info_db", "Erro ao remover tarefa!")
            return false
        }

        return true
    }

    override fun listarTarefa(): List<Tarefa> {
        val listaTarefas = mutableListOf<Tarefa>()

        val comandoSQL = "SELECT ${DatabaseHelper.COLUNA_ID_TAREFA}, ${DatabaseHelper.COLUNA_DESCRICAO}, strftime('%d/%m/%Y', ${DatabaseHelper.COLUNA_DATA_CADASTRO}) AS ${DatabaseHelper.COLUNA_DATA_CADASTRO} FROM ${DatabaseHelper.NOME_TABELA_TAREFAS};"

        val cursor = leitura.rawQuery(comandoSQL, null)

        val indiceID = cursor.getColumnIndex(DatabaseHelper.COLUNA_ID_TAREFA)
        val indiceDescricao = cursor.getColumnIndex(DatabaseHelper.COLUNA_DESCRICAO)
        val indiceData = cursor.getColumnIndex(DatabaseHelper.COLUNA_DATA_CADASTRO)

        while (cursor.moveToNext()) {
            val colunaIdTarefa = cursor.getInt(indiceID)
            val colunaDescricao = cursor.getString(indiceDescricao)
            val colunaDataCadastro = cursor.getString(indiceData)

            listaTarefas.add(
                Tarefa(colunaIdTarefa, colunaDescricao, colunaDataCadastro )
            )
        }


        return listaTarefas
    }
}