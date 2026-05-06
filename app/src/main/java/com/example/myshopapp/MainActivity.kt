package com.example.myshopapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myshopapp.ui.theme.MyshopappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyshopappTheme {
                ShopApp()
            }
        }
    }
}

@Composable
fun ShopApp(viewModel: ShopViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf("onboarding") }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    when (currentScreen) {
        "onboarding" -> OnboardingScreen { currentScreen = "home" }
        "home" -> HomeScreen(
            onProductClick = {
                selectedProduct = it
                currentScreen = "details"
            },
            onBagClick = { currentScreen = "bag" },
            viewModel = viewModel
        )
        "details" -> ProductDetailScreen(
            product = selectedProduct!!,
            onBack = { currentScreen = "home" },
            onAddToCart = {
                viewModel.addToCart(it)
                currentScreen = "bag"
            }
        )
        "bag" -> BagScreen(
            viewModel = viewModel,
            onBack = { currentScreen = "home" },
            onCheckout = {
                viewModel.clearCart()
                currentScreen = "home"
            }
        )
    }
}

@Composable
fun OnboardingScreen(onGetStarted: () -> Unit) {
    val context = LocalContext.current
    val heroImageRes = context.resources.getIdentifier("hoodie", "drawable", context.packageName)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.2f)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFE0F2F1), Color.White)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "URBAN",
                    color = Color.Black.copy(alpha = 0.05f),
                    fontSize = 120.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(y = (-40).dp)
                )
                if (heroImageRes != 0) {
                    Image(
                        painter = painterResource(id = heroImageRes),
                        contentDescription = null,
                        modifier = Modifier.size(300.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Define Your\nModern Style",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 44.sp,
                        fontSize = 38.sp
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Explore the best collection of modern apparel and premium fashion essentials tailored for you.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(40.dp))
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Get Started", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onProductClick: (Product) -> Unit, onBagClick: () -> Unit, viewModel: ShopViewModel) {
    val totalPrice by viewModel.totalPrice.collectAsState()
    var selectedCategory by remember { mutableStateOf("All") }

    Scaffold(
        containerColor = Color(0xFFFBFBFF),
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Black)
                    }
                    IconButton(onClick = onBagClick) {
                        BadgedBox(
                            badge = {
                                if (viewModel.cartItems.isNotEmpty()) {
                                    Badge(containerColor = Color.Red) {
                                        Text(viewModel.cartItems.size.toString(), color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Bag", tint = Color.Gray)
                        }
                    }
                    Text(
                        text = "Total: $${String.format("%.2f", totalPrice)}",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.Gray)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.Gray)
                    }
                }
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 120.dp
            ),
            userScrollEnabled = true,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item(span = { GridItemSpan(maxLineSpan) }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {}) { Icon(Icons.Default.Menu, contentDescription = "Menu") }
                    Text("Urban Shop", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Search") }
                }
            }

            // Banner
            item(span = { GridItemSpan(maxLineSpan) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("NEW SEASON", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(
                                "Modern\nEssentials",
                                color = Color.Black,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 30.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Shop Now", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.ThumbUp,
                                contentDescription = null,
                                tint = Color(0xFFE0E0E0),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }

            // Categories
            item(span = { GridItemSpan(maxLineSpan) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(listOf("All", "T-Shirts", "Jeans", "Hoodies", "Shirts")) { category ->
                        val isSelected = category == selectedCategory
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color.Black else Color(0xFFF7F7F7),
                            modifier = Modifier.clickable { selectedCategory = category }
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Product List Title
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    "New Arrivals",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Grid Items
            items(sampleProducts) { product ->
                ProductItem(product = product, onClick = { onProductClick(product) })
            }

            // Footer Spacer
            item(span = { GridItemSpan(maxLineSpan) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageRes = context.resources.getIdentifier(product.imageResName, "drawable", context.packageName)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF7F7F7)),
                contentAlignment = Alignment.Center
            ) {
                if (imageRes != 0) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                product.category,
                color = Color.Gray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                product.name,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                maxLines = 1,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$${String.format("%.2f", product.price)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Surface(
                    onClick = onClick,
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color.Black
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(product: Product, onBack: () -> Unit, onAddToCart: (Product) -> Unit) {
    val context = LocalContext.current
    val imageRes = context.resources.getIdentifier(product.imageResName, "drawable", context.packageName)
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .background(Color(0xFFF7F7F7), RoundedCornerShape(12.dp))
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text("Product Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .background(Color(0xFFF7F7F7), RoundedCornerShape(12.dp))
                        .size(40.dp)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(Color(0xFFF7F7F7)),
                contentAlignment = Alignment.Center
            ) {
                if (imageRes != 0) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(40.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        product.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        lineHeight = 32.sp
                    )
                    Text(
                        product.category,
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    "$${String.format("%.2f", product.price)}",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    " 4.9 (520 Reviews)",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("About the Product", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                product.description,
                color = Color.Gray,
                fontSize = 15.sp,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Select Size", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("S", "M", "L", "XL", "XXL").forEach { size ->
                    val isSelected = size == "M"
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color.Black else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) Color.Black else Color(0xFFEEEEEE)
                        ),
                        modifier = Modifier.clickable { }
                    ) {
                        Text(
                            text = size,
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 14.dp),
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onAddToCart(product) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(18.dp)
            ) {
                Text("Add to Bag", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BagScreen(viewModel: ShopViewModel, onBack: () -> Unit, onCheckout: () -> Unit) {
    val cartItems = viewModel.cartItems
    val totalPrice by viewModel.totalPrice.collectAsState()
    val context = LocalContext.current

    Scaffold(
        containerColor = Color.White,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Bag", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(100.dp),
                            tint = Color(0xFFF0F0F0)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Your bag is empty", color = Color.Gray, fontSize = 18.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(cartItems) { product ->
                        val imageRes = context.resources.getIdentifier(product.imageResName, "drawable", context.packageName)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF9F9F9), RoundedCornerShape(24.dp))
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .background(Color.White, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (imageRes != 0) {
                                    Image(
                                        painter = painterResource(id = imageRes),
                                        contentDescription = product.name,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(product.name, fontWeight = FontWeight.Bold, maxLines = 1, fontSize = 16.sp)
                                Text(product.category, color = Color.Gray, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "$${String.format("%.2f", product.price)}",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp
                                )
                            }
                            IconButton(
                                onClick = { viewModel.removeFromCart(product) },
                                modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFD32F2F))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Price", fontSize = 18.sp, color = Color.Gray)
                    Text(
                        "$${String.format("%.2f", totalPrice)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onCheckout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Checkout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
