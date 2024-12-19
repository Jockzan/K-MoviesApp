package com.jcb.moviesapp.presentation.ui.movies.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jcb.moviesapp.R
import com.jcb.moviesapp.domain.models.Movie
import com.jcb.moviesapp.presentation.ui.common.ErrorMessage
import com.jcb.moviesapp.presentation.ui.navigation.MovieDetails
import com.jcb.moviesapp.presentation.ui.theme.Dimens
import com.jcb.moviesapp.presentation.ui.theme.Typography
import com.jcb.moviesapp.presentation.utils.Constants
import com.jcb.moviesapp.presentation.utils.DateUtils.toMedium

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    navController: NavController
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(searchQuery.text) {
        viewModel.updateSearchQuery(searchQuery.text)
    }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .imePadding()
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(
            Modifier.windowInsetsTopHeight(
                WindowInsets.statusBars
            )
        )

        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChanged = { searchQuery = it }
        )

        listOf(
            MoviesViewModel.CATEGORY_POPULAR,
            MoviesViewModel.CATEGORY_NOW_PLAYING,
            MoviesViewModel.CATEGORY_TOP_RATED,
            MoviesViewModel.CATEGORY_UPCOMING
        ).forEach { category ->
            val titleRes = when (category) {
                MoviesViewModel.CATEGORY_POPULAR -> R.string.popular
                MoviesViewModel.CATEGORY_NOW_PLAYING -> R.string.now_playing
                MoviesViewModel.CATEGORY_TOP_RATED -> R.string.top_rated
                MoviesViewModel.CATEGORY_UPCOMING -> R.string.upcoming
                else -> R.string.app_name
            }
            MovieCategory(
                title = stringResource(titleRes),
                movies = viewModel.filteredMoviesMap[category]?.value.orEmpty(),
                isLoading = viewModel.isLoadingMap[category]?.value ?: false,
                showError = viewModel.showErrorMap[category]?.value ?: false,
                onLoadMore = { viewModel.fetchMovies(category) },
                navController = navController,
                getGenreNameById = viewModel::getGenreNameById,
                onRetry = { viewModel.fetchMovies(category) }
            )
        }

        Spacer(
            Modifier.windowInsetsBottomHeight(
                WindowInsets.systemBars
            )
        )
    }
}

@Composable
fun SearchBar(
    searchQuery: TextFieldValue,
    onSearchQueryChanged: (TextFieldValue) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens().paddingSmall)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small
            )
            .padding(Dimens().paddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(id = R.string.search_placeholder),
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(end = Dimens().paddingSmall)
        )
        BasicTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChanged,
            singleLine = true,
            textStyle = Typography().bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions.Default,
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (searchQuery.text.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.search_placeholder),
                        style = Typography().bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
fun MovieCategory(
    title: String,
    movies: List<Movie>,
    isLoading: Boolean,
    showError: Boolean,
    onLoadMore: () -> Unit,
    navController: NavController,
    getGenreNameById: (Int) -> String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens().paddingMedium)
        )

        if (showError) {
            ErrorMessage(onRetry = onRetry)
        } else if (movies.isEmpty()) {
            Text(
                text = stringResource(R.string.no_movies_found),
                style = Typography().bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens().paddingMedium),
                textAlign = TextAlign.Center
            )
        } else {
            LazyRow(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(movies) { movie ->
                    MovieCard(
                        movie = movie,
                        navController = navController,
                        getGenreNameById = getGenreNameById
                    )
                }

                item {
                    LoadMoreItem(
                        isLoading = isLoading,
                        onLoadMore = onLoadMore
                    )
                }
            }
        }
    }
}

@Composable
fun MovieCard(
    movie: Movie,
    navController: NavController,
    getGenreNameById: (Int) -> String
) {
    val model = "${Constants.IMAGE_URL}${movie.backdropPath}"
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = screenWidth / 1.3f

    Card(
        shape = RoundedCornerShape(Dimens().roundedCornerSmall),
        elevation = CardDefaults.cardElevation(Dimens().cardElevation),
        modifier = Modifier
            .width(cardWidth)
            .height(Dimens().cardHeight)
            .padding(Dimens().paddingSmall)
            .clickable {
                navController.navigate(MovieDetails(moveId = movie.id))
            }
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(model)
                    .crossfade(true)
                    .build(),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black),
                            startY = 10f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Dimens().paddingMedium)
            ) {
                Text(
                    text = movie.title,
                    color = Color.White,
                    style = Typography().bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.overview,
                    color = Color.White.copy(alpha = 0.8f),
                    style = Typography().bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.genreIds.joinToString(", ") { getGenreNameById(it) },
                    color = Color.White.copy(alpha = 0.6f),
                    style = Typography().bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = movie.releaseDate.toMedium(),
                    color = Color.White.copy(alpha = 0.6f),
                    style = Typography().bodySmall,
                )
            }
        }
    }
}


@Composable
fun LoadMoreItem(
    isLoading: Boolean,
    onLoadMore: () -> Unit
) {
    if (isLoading) {
        Text(
            text = stringResource(R.string.loading),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(Dimens().paddingMedium)
        )
    } else {
        Spacer(modifier = Modifier.width(Dimens().paddingMedium))
        Text(
            text = stringResource(R.string.load_more),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(Dimens().paddingMedium)
                .clickable { onLoadMore() }
        )
    }
}
