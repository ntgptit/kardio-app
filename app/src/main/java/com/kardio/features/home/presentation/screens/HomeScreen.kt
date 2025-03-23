package com.kardio.features.dashboard.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kardio.R
import com.kardio.features.dashboard.domain.model.Class
import com.kardio.features.dashboard.domain.model.Folder
import com.kardio.features.dashboard.domain.model.StreakDay
import com.kardio.features.dashboard.domain.model.StudyModule
import com.kardio.features.dashboard.presentation.viewmodel.DashboardViewModel
import com.kardio.ui.components.feedback.KardioFullScreenLoading
import com.kardio.ui.components.feedback.KardioSnackbarHost
import com.kardio.ui.theme.BackgroundSecondary
import com.kardio.ui.theme.Divider
import com.kardio.ui.theme.Secondary
import com.kardio.ui.theme.StreakFlameOrange
import com.kardio.ui.theme.TextPrimary
import com.kardio.ui.theme.TextSecondary

/**
 * Dashboard Screen - màn hình chính của ứng dụng
 */
@Composable
fun DashboardScreen(
    navigateToFolderDetail: (String, String) -> Unit,
    navigateToClassDetail: (String, String) -> Unit,
    navigateToModuleDetail: (String) -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Hiển thị error nếu có
    uiState.error?.let {
        // TODO: Hiển thị error message
    }

    Scaffold(
        snackbarHost = { KardioSnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            DashboardHeader()

            // Search Bar
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            // Folder section
            SectionHeader(
                title = "Thư mục",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Folders list
            FoldersList(
                folders = uiState.folders,
                onFolderClick = { folder ->
                    navigateToFolderDetail(folder.id, folder.name)
                }
            )

            // Thành tựu section
            SectionHeader(
                title = "Thành tựu",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Streak card
            uiState.streakData?.let { streakData ->
                StreakCard(
                    streakCount = streakData.currentStreak,
                    streakDays = streakData.weeklyStreak,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }

            // Học phần section
            SectionHeader(
                title = "Học phần",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Study modules list
            StudyModulesList(
                modules = uiState.studyModules,
                onModuleClick = { module ->
                    navigateToModuleDetail(module.id)
                }
            )

            // Lớp học section
            SectionHeader(
                title = "Lớp học",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Classes list
            ClassesList(
                classes = uiState.classes,
                onClassClick = { classItem ->
                    navigateToClassDetail(classItem.id, classItem.name)
                }
            )

            // Bottom spacer
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Loading indicator
        KardioFullScreenLoading(
            isLoading = uiState.isLoading,
            message = "Đang tải dữ liệu..."
        )
    }
}

/**
 * Dashboard Header với title và notification icon
 */
@Composable
fun DashboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Kardio Plus",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        IconButton(
            onClick = { /* TODO: Handle notification click */ }
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = TextPrimary
            )
        }
    }
}

/**
 * Search Bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    OutlinedCard(
        onClick = { /* TODO: Navigate to search screen */ },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = BackgroundSecondary
        ),
        border = null,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = TextSecondary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Học phần, sách giáo khoa, câu hỏi",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}

/**
 * Section header
 */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = TextPrimary,
        modifier = modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

/**
 * List of folders
 */
@Composable
fun FoldersList(
    folders: List<Folder>,
    onFolderClick: (Folder) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(folders.size) { index ->
            FolderItem(
                folder = folders[index],
                onClick = { onFolderClick(folders[index]) }
            )
        }
    }
}

/**
 * Folder item
 */
@Composable
fun FolderItem(
    folder: Folder,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundSecondary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // TODO: Implement folder item UI
        }
    }
}

/**
 * Streak card
 */
@Composable
fun StreakCard(
    streakCount: Int,
    streakDays: List<StreakDay>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = BackgroundSecondary
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Divider
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Chuỗi $streakCount tuần",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            // Streak icon and count
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_streak_flame),
                    contentDescription = "Streak flame",
                    tint = StreakFlameOrange,
                    modifier = Modifier.size(64.dp)
                )

                Text(
                    text = "$streakCount",
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Message
            Text(
                text = "Hãy học vào tuần tới để duy trì chuỗi của bạn!",
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            // Week days
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Streak day indicators
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = Secondary
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    streakDays.forEach { streakDay ->
                        StreakDayItem(
                            day = streakDay.day,
                            hasCompleted = streakDay.hasCompleted
                        )
                    }
                }
            }
        }
    }
}

/**
 * Streak day item
 */
@Composable
fun StreakDayItem(
    day: Int,
    hasCompleted: Boolean
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        if (hasCompleted) {
            Icon(
                painter = painterResource(id = R.drawable.ic_streak_flame),
                contentDescription = null,
                tint = StreakFlameOrange,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = if (hasCompleted) TextPrimary else TextSecondary
        )
    }
}

/**
 * List of study modules
 */
@Composable
fun StudyModulesList(
    modules: List<StudyModule>,
    onModuleClick: (StudyModule) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(modules.size) { index ->
            StudyModuleItem(
                module = modules[index],
                onClick = { onModuleClick(modules[index]) }
            )
        }
    }
}

/**
 * Study module item
 */
@Composable
fun StudyModuleItem(
    module: StudyModule,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundSecondary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Title
            Text(
                text = module.title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Term count
            Text(
                text = "${module.termCount} thuật ngữ",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Creator info
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar (placeholder)
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Username
                Text(
                    text = module.createdByUsername,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                // Plus badge if applicable
                if (module.isCreatedByPlusUser) {
                    Spacer(modifier = Modifier.width(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Gray.copy(alpha = 0.3f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "PLUS",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

/**
 * List of classes
 */
@Composable
fun ClassesList(
    classes: List<Class>,
    onClassClick: (Class) -> Unit
) {
    androidx.compose.foundation.lazy.LazyRow(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(classes.size) { index ->
            ClassItem(
                classItem = classes[index],
                onClick = { onClassClick(classes[index]) }
            )
        }
    }
}

/**
 * Class item
 */
@Composable
fun ClassItem(
    classItem: Class,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundSecondary
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Class name
            Text(
                text = classItem.name,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Separator
            Text(
                text = "•",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Stats
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Modules count
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_folder),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${classItem.studyModuleCount} học phần",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }

                // Members count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_people),
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "${classItem.memberCount} thành viên",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}