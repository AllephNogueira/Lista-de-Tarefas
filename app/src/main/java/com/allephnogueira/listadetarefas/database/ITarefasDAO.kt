package com.allephnogueira.listadetarefas.database

import com.allephnogueira.listadetarefas.model.Tarefa

interface ITarefasDAO {

    fun salvar( tarefa: Tarefa ) : Boolean
    fun atualizar( tarefa: Tarefa ) : Boolean
    fun remover ( idTarefa: Int ) : Boolean
    fun listarTarefa () : List<Tarefa>
}