package com.feiradagua.feiradagua.model.`class`


class TutorialData {
    fun setUpProducerRecyclerView(): MutableList<Producer> {
        val producer1 = Producer("", "Feira D'água", "","(XX) 9XXXX-XXXX",
            "",0, "Avenida Beira Mar, 1000, 102, Centro, Florianópolis - SC",
            "Bem vindo ao nosso Perfil, temos os produtos mais frescos do mercado!! Venha conferir!!",
            mutableListOf("Segunda","Terça", "Quarta"), mutableListOf("Florianópolis - Centro", "Florianópolis - Norte"),
            mutableListOf("Pix", "Cartão de Débito"), mutableListOf("Peixes, Camarão"), "")

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

    fun setUpNewProduct(): Products {
        return Products("", "Tainha Ovada", "Tainha fresquinha com ovas. Cortamos ela de acordo com " +
                " a sua vontade, seja em postas ou espalmada.",18.50, "", "")
    }

    fun setUpOrderRecyclerView(): MutableList<Order> {
        val order1 = Order("", mutableListOf(), "Antônio Chaves", "", 150.0, "10/10/2021",
        0, mutableListOf(), "")
        val order2 = Order("", mutableListOf(), "Jéssica Nascimento", "", 40.0, "05/10/2021",
                0, mutableListOf(), "")

        return  mutableListOf(order1, order2)
    }
}