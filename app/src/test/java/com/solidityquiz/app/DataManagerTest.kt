package com.solidityquiz.app

import android.content.Context
import android.content.SharedPreferences
import com.solidityquiz.app.data.DataManager
import com.solidityquiz.app.models.QuestionType
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class DataManagerTest {

    @Mock
    private lateinit var mockContext: Context
    
    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences
    
    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor
    
    private lateinit var dataManager: DataManager

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        `when`(mockContext.getSharedPreferences(anyString(), anyInt()))
            .thenReturn(mockSharedPreferences)
        `when`(mockSharedPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putBoolean(anyString(), anyBoolean())).thenReturn(mockEditor)
        `when`(mockEditor.putInt(anyString(), anyInt())).thenReturn(mockEditor)
        
        dataManager = DataManager(mockContext)
    }

    @Test
    fun testGetAllLevels_ReturnsCorrectNumberOfLevels() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val levels = dataManager.getAllLevels()
        
        // Then
        assertEquals(5, levels.size)
        assertEquals("ERC-20 Token Contract", levels[0].title)
        assertEquals("ERC-721 NFT Contract", levels[1].title)
        assertEquals("Crowdfunding via NFT", levels[2].title)
        assertEquals("DAO Contract", levels[3].title)
        assertEquals("AMM Contract & React Frontend", levels[4].title)
    }

    @Test
    fun testLevel1_HasCorrectLessons() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val level1 = dataManager.getLevelById(1)
        
        // Then
        assertNotNull(level1)
        assertEquals(2, level1!!.lessons.size)
        assertEquals("ERC-20 Token Basics", level1.lessons[0].title)
        assertEquals("Token Transfer Function", level1.lessons[1].title)
    }

    @Test
    fun testLevel1_HasCorrectQuestions() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val level1 = dataManager.getLevelById(1)
        val lesson1Questions = level1!!.lessons[0].quizQuestions
        
        // Then
        assertTrue(lesson1Questions.isNotEmpty())
        assertEquals(QuestionType.FILL_IN_THE_BLANK, lesson1Questions[0].type)
        assertEquals(QuestionType.MULTIPLE_CHOICE, lesson1Questions[1].type)
    }

    @Test
    fun testLevel1_IsAlwaysUnlocked() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        
        // When
        val isUnlocked = dataManager.isLevelUnlocked(1)
        
        // Then
        assertTrue(isUnlocked)
    }

    @Test
    fun testLevel2_IsLockedInitially() {
        // Given
        `when`(mockSharedPreferences.getBoolean("level_1_completed", false)).thenReturn(false)
        `when`(mockSharedPreferences.getBoolean("level_2_unlocked", false)).thenReturn(false)
        
        // When
        val isUnlocked = dataManager.isLevelUnlocked(2)
        
        // Then
        assertFalse(isUnlocked)
    }

    @Test
    fun testMarkLessonCompleted_UpdatesProgress() {
        // Given
        val levelId = 1
        val lessonId = 1
        
        // When
        dataManager.markLessonCompleted(levelId, lessonId)
        
        // Then
        verify(mockEditor).putBoolean("lesson_${levelId}_${lessonId}_completed", true)
        verify(mockEditor).apply()
    }

    @Test
    fun testGetLessonById_ReturnsCorrectLesson() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val lesson = dataManager.getLessonById(1, 1)
        
        // Then
        assertNotNull(lesson)
        assertEquals("ERC-20 Token Basics", lesson!!.title)
        assertEquals(1, lesson.levelId)
        assertEquals(1, lesson.id)
    }

    @Test
    fun testLevel2_HasNFTContent() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val level2 = dataManager.getLevelById(2)
        
        // Then
        assertNotNull(level2)
        assertEquals(2, level2!!.lessons.size)
        assertTrue(level2.lessons[0].codeContent.contains("ERC721"))
        assertTrue(level2.lessons[0].explanation.contains("non-fungible"))
    }

    @Test
    fun testLevel3_HasCrowdfundingContent() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val level3 = dataManager.getLevelById(3)
        
        // Then
        assertNotNull(level3)
        assertEquals(3, level3!!.lessons.size)
        assertTrue(level3.lessons[0].codeContent.contains("CrowdfundingNFT"))
        assertTrue(level3.lessons[1].codeContent.contains("contribute"))
    }

    @Test
    fun testLevel4_HasDAOContent() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val level4 = dataManager.getLevelById(4)
        
        // Then
        assertNotNull(level4)
        assertEquals(3, level4!!.lessons.size)
        assertTrue(level4.lessons[0].codeContent.contains("SimpleDAO"))
        assertTrue(level4.lessons[1].codeContent.contains("vote"))
        assertTrue(level4.lessons[2].codeContent.contains("executeProposal"))
    }

    @Test
    fun testLevel5_HasAMMContent() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val level5 = dataManager.getLevelById(5)
        
        // Then
        assertNotNull(level5)
        assertEquals(3, level5!!.lessons.size)
        assertTrue(level5.lessons[0].codeContent.contains("AMM"))
        assertTrue(level5.lessons[1].codeContent.contains("swap"))
        assertTrue(level5.lessons[2].codeContent.contains("React"))
    }

    @Test
    fun testQuizQuestions_HaveCorrectStructure() {
        // Given
        `when`(mockSharedPreferences.getBoolean(anyString(), anyBoolean())).thenReturn(false)
        `when`(mockSharedPreferences.getInt(anyString(), anyInt())).thenReturn(0)
        
        // When
        val level1 = dataManager.getLevelById(1)
        val questions = level1!!.lessons[0].quizQuestions
        
        // Then
        questions.forEach { question ->
            assertTrue(question.id > 0)
            assertTrue(question.question.isNotEmpty())
            assertTrue(question.correctAnswer.isNotEmpty())
            assertTrue(question.explanation.isNotEmpty())
            
            when (question.type) {
                QuestionType.MULTIPLE_CHOICE -> {
                    assertNotNull(question.options)
                    assertTrue(question.options!!.size >= 2)
                }
                QuestionType.FILL_IN_THE_BLANK -> {
                    assertNotNull(question.blanks)
                    assertTrue(question.blanks!!.isNotEmpty())
                }
                QuestionType.CODE_COMPLETION -> {
                    // Code completion questions should have code snippets
                    assertNotNull(question.codeSnippet)
                }
            }
        }
    }
}
