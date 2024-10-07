package com.pjff.firebasenotes.views.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.pjff.firebasenotes.viewModels.LoginViewModel

//Vid 173
@Composable
//Vid 175,add navController: NavController, loginVM: LoginViewMode
fun TabsView(navController: NavController, loginVM: LoginViewModel) {

    var selectedTab by remember { mutableStateOf(0) }
    //Ponemos nuestros tabs
    val tabs = listOf("Iniciar Sesion", "Registrarse")

    Column {
        TabRow(selectedTabIndex = selectedTab,
            contentColor = Color.Black,
            //Para darle color al tab
            containerColor = Color.LightGray,
            //Vid 173, el indicator nos sirve para ver las posiciones de los tabs
            indicator = { tabPosition ->
                TabRowDefaults.Indicator(
                    Modifier.tabIndicatorOffset(tabPosition[selectedTab])
                )
            }
        )
        {
            //Vid 173, sacamos el número de elementos , para saber cuantos tabs tenemos.
            tabs.forEachIndexed { index, title ->
                Tab(selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(text = title) }
                )
            }
        }
        //Vid 173,selecionaremos el login y el registerView
        when(selectedTab){
            //Vid 175
            0 -> LoginView(navController, loginVM)
            1 -> RegisterView(navController, loginVM)
        }
    }

}















