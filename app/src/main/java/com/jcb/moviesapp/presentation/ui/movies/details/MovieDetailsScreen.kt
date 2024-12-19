package com.jcb.moviesapp.presentation.ui.movies.details

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jcb.moviesapp.R
import com.jcb.moviesapp.domain.models.ProductionCompany
import com.jcb.moviesapp.domain.models.Video
import com.jcb.moviesapp.presentation.ui.common.ErrorMessage
import com.jcb.moviesapp.presentation.ui.theme.Dimens
import com.jcb.moviesapp.presentation.ui.theme.Typography
import com.jcb.moviesapp.presentation.utils.Constants
import com.jcb.moviesapp.presentation.utils.DateUtils.toMedium

@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    movieId: Int = 1,
    navController: NavController
) {
    LaunchedEffect(movieId) {
        viewModel.getMovieDetails(movieId)
        viewModel.getMovieVideos(movieId)
    }

    val scrollState = rememberScrollState()

    val movieDetails = viewModel.movieDetails

    AnimatedVisibility(viewModel.showError) {
        ErrorMessage(
            onRetry = {
                viewModel.getMovieDetails(movieId)
                viewModel.getMovieVideos(movieId)
            }
        )
    }

    AnimatedVisibility(movieDetails != null) {
        Box(
            modifier = Modifier
                .imePadding()
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {

            Column {
                val model = "${Constants.IMAGE_URL}${movieDetails?.backdropPath}"
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth(),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(model)
                        .crossfade(true)
                        .build(),
                    contentDescription = movieDetails?.title,
                    contentScale = ContentScale.Fit,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = movieDetails?.title.orEmpty(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = Typography().bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens().paddingMedium)
                    )
                    if (movieDetails?.tagline != null) {
                        Text(
                            text = movieDetails.tagline,
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Typography().bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens().paddingMedium),
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Text(
                        text = movieDetails?.overview.orEmpty(),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Justify,
                        style = Typography().bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = Dimens().paddingMedium,
                                vertical = Dimens().paddingSmall
                            )
                    )
                    if (viewModel.movieVideos.isNotEmpty()) {
                        Text(
                            text = stringResource(R.string.trailers),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Typography().bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = Dimens().paddingMedium,
                                    end = Dimens().paddingMedium,
                                    top = Dimens().paddingMedium
                                )
                        )
                        MoviesPreviewContent(
                            videos = viewModel.movieVideos.filter {
                                it.site.contains("YouTube", true)
                            },
                            context = navController.context
                        )
                    }
                    if (movieDetails?.genres?.isNotEmpty() == true) {
                        Text(
                            text = stringResource(R.string.genres),
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Typography().bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = Dimens().paddingMedium,
                                    end = Dimens().paddingMedium,
                                    top = Dimens().paddingMedium
                                )
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Dimens().paddingMedium),
                            text = movieDetails.genres.joinToString { it.name },
                            color = MaterialTheme.colorScheme.onBackground,
                            style = Typography().bodySmall,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Text(
                        text = stringResource(R.string.release_date_label),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = Typography().bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = Dimens().paddingMedium,
                                end = Dimens().paddingMedium,
                                top = Dimens().paddingMedium
                            )
                    )
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Dimens().paddingMedium),
                        text = movieDetails?.releaseDate?.toMedium().orEmpty(),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = Typography().bodySmall,
                        fontStyle = FontStyle.Italic
                    )
                    if (movieDetails?.productionCompanies?.isNotEmpty() == true) {
                        MoviesProductionCompaniesContent(
                            movieDetails.productionCompanies
                        )
                    }
                    val movPst = "${Constants.IMAGE_URL}${movieDetails?.posterPath}"
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                            .padding(
                                start = Dimens().paddingMedium,
                                end = Dimens().paddingMedium,
                                top = Dimens().paddingMedium
                            ),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movPst)
                            .crossfade(true)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .build(),
                        contentDescription = movieDetails?.title,
                        contentScale = ContentScale.Fit,
                    )
                    Spacer(
                        Modifier.windowInsetsBottomHeight(
                            WindowInsets.systemBars
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(Dimens().paddingMedium)
                    .align(Alignment.TopStart)
                    .clickable { navController.popBackStack() }
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = Dimens().paddingLarge),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(Dimens().iconSize)
                    )
                    Spacer(modifier = Modifier.width(Dimens().paddingSmall))
                    Text(
                        text = stringResource(R.string.back),
                        color = Color.White,
                        style = Typography().bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun MoviesPreviewContent(
    videos: List<Video>,
    context: Context,
) {
    LazyRow(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens().paddingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens().paddingSmall)
    ) {
        items(videos) { video ->
            val model =
                "${Constants.YOUTUBE_IMAGE_URL_PRE}${video.key}${Constants.YOUTUBE_IMAGE_URL_POST}"
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(Dimens().imageWidthSmall)
                        .height(Dimens().imageHeightSmall)
                        .clickable { openYouTube(videoId = video.key, context = context) },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(model)
                        .crossfade(true)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .build(),
                    contentDescription = video.site,
                    contentScale = ContentScale.Fit,
                )
                Text(
                    modifier = Modifier
                        .width(Dimens().imageWidthSmall),
                    text = video.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    style = Typography().bodyXSmall,
                    fontStyle = FontStyle.Italic,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}

@Composable
private fun MoviesProductionCompaniesContent(
    companies: List<ProductionCompany>
) {
    LazyRow(
        modifier =
        Modifier
            .height(Dimens().imageWidthSmall)
            .fillMaxWidth()
            .padding(Dimens().paddingMedium),
        horizontalArrangement = Arrangement.spacedBy(Dimens().paddingSmall)
    ) {
        items(companies) { company ->
            val model = "${Constants.IMAGE_URL}${company.logoPath}"
            Column(
                verticalArrangement = Arrangement.Center,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(Dimens().productionCompanySize)
                        .height(Dimens().productionCompanySize),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(model)
                        .crossfade(true)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .build(),
                    contentDescription = company.name,
                    contentScale = ContentScale.Fit,
                    colorFilter = if (isSystemInDarkTheme()) ColorFilter.tint(Color.White) else null
                )
            }
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
private fun openYouTube(videoId: String, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
    intent.putExtra("VIDEO_ID", videoId)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        val webIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse("${Constants.YOUTUBE_URL}$videoId"))
        context.startActivity(webIntent)
    }
}