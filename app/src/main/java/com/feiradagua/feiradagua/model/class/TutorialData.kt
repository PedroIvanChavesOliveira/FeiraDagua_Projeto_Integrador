package com.feiradagua.feiradagua.model.`class`


class TutorialData {
    fun setUpProducerRecyclerView(): MutableList<Producer> {
        val producer1 = Producer("", "Feira D'água", "","(XX) 9XXXX-XXXX",
            "",0, "Avenida Beira Mar, 1000, 102, Centro, Florianópolis - SC",
            "Bem vindo ao nosso Perfil, temos os produtos mai frescos do mercado!! Venha conferir!!",
            mutableListOf("Segunda","Terça", "Quarta"), mutableListOf("Florianópolis - Centro", "Florianópolis - Norte"),
            mutableListOf("Pix", "Cartão de Débito"),"")

        return mutableListOf(producer1)
    }

    fun setUpProductsRecyclerView(): MutableList<Products> {
        val product1 = Products("", "Tainha Ovada", "Tainha fresquinha com ovas. Cortamos ela de acordo com " +
                " a sua vontade, seja em postas ou espalmada.",18.50, "", "")
        val product2 = Products("", "Ostra Em Natura", "Vendemos a dúzia da Ostra, coleta hoje e com um sabor" +
                "indescritível de bom!!",15.0, "", "")
        return mutableListOf(product1, product2)
    }

    fun setUpCartRecyclerView(): MutableList<Cart> {
        val cart = Cart("","Tainha Ovada","Tainha fresquinha com ovas. Cortamos ela de acordo com " +
                " a sua vontade, seja em postas ou espalmada.", 18.50, 37.0, "", "")

        return mutableListOf(cart)
    }
}