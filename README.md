# Solidity Learning Android App

An interactive Android application designed to teach Solidity programming through hands-on lessons and quizzes. The app features 5 progressive levels covering essential smart contract development concepts.

## Features

### 📚 5 Learning Levels
1. **ERC-20 Token Contract** - Basic token creation and JavaScript testing
2. **ERC-721 NFT Contract** - Non-fungible token development and testing
3. **Crowdfunding via NFT Minting** - Advanced contract interactions
4. **DAO Contract** - Decentralized governance and voting mechanisms
5. **AMM Contract & React Frontend** - Automated Market Maker with frontend integration

### 🎯 Interactive Learning
- **Code Lessons**: Syntax-highlighted Solidity code with detailed explanations
- **Fill-in-the-Blank Questions**: Interactive code completion exercises
- **Multiple Choice Quizzes**: Concept understanding verification
- **Progress Tracking**: Monitor advancement through all levels

### 🛠 Technical Features
- Modern Android architecture with Kotlin
- Syntax highlighting for Solidity code
- Persistent progress tracking
- Material Design UI components
- Responsive layouts for different screen sizes

## Project Structure

```
quiz_app/
├── app/
│   ├── src/main/
│   │   ├── java/com/solidityquiz/app/
│   │   │   ├── adapters/          # RecyclerView adapters
│   │   │   ├── data/              # Data management and lesson content
│   │   │   ├── models/            # Data models
│   │   │   ├── MainActivity.kt    # Main level selection screen
│   │   │   ├── LessonActivity.kt  # Code lesson display
│   │   │   ├── QuizActivity.kt    # Interactive quiz interface
│   │   │   └── ProgressActivity.kt # Progress tracking
│   │   └── res/
│   │       ├── layout/            # XML layouts
│   │       ├── drawable/          # Icons and graphics
│   │       ├── values/            # Colors, strings, themes
│   │       └── ...
│   └── build.gradle
├── build.gradle
├── settings.gradle
└── README.md
```

## Key Components

### Data Models
- **Level**: Represents a learning level with lessons and progress
- **Lesson**: Contains code content, explanations, and quiz questions
- **QuizQuestion**: Supports multiple choice and fill-in-the-blank formats
- **UserProgress**: Tracks completion status and scores

### Activities
- **MainActivity**: Level selection with progress overview
- **LessonActivity**: Displays code lessons with syntax highlighting
- **QuizActivity**: Interactive quiz interface with immediate feedback
- **ProgressActivity**: Comprehensive progress tracking

### Adapters
- **LevelAdapter**: Displays available levels with progress indicators
- **LessonAdapter**: Shows lesson content with code highlighting
- **ProgressAdapter**: Detailed progress breakdown by level

## Learning Content

### Level 1: ERC-20 Token Contract
- Token contract structure and state variables
- Transfer function implementation
- JavaScript testing with Hardhat
- Balance checking and event emission

### Level 2: ERC-721 NFT Contract
- Non-fungible token concepts
- Minting and ownership functions
- Metadata handling
- NFT-specific testing patterns

### Level 3: Crowdfunding via NFT (Planned)
- Crowdfunding contract mechanics
- NFT rewards system
- Goal tracking and fund distribution
- Advanced testing scenarios

### Level 4: DAO Contract (Planned)
- Governance token implementation
- Proposal creation and voting
- Execution mechanisms
- DAO testing strategies

### Level 5: AMM Contract & React Frontend (Planned)
- Automated Market Maker logic
- Liquidity pool management
- React/Redux frontend integration
- End-to-end testing

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later (recommended: Android Studio Flamingo)
- Android SDK 24 or higher (Android 7.0+)
- Kotlin 1.8.20+
- Gradle 8.0+
- JDK 11 or higher

### Installation
1. Clone or download the project to your local machine
2. Open Android Studio
3. Select "Open an Existing Project" and navigate to the `quiz_app` folder
4. Wait for Gradle sync to complete
5. Ensure you have the required SDK versions installed via SDK Manager
6. Run the app on an emulator or physical device

### Building
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Generate test coverage report
./gradlew jacocoTestReport
```

### Project Setup Verification
To verify the project is set up correctly:

1. **Check Gradle Sync**: Ensure no errors in the Gradle sync
2. **Verify Dependencies**: All dependencies should download successfully
3. **Run Tests**: Execute `./gradlew test` to ensure all unit tests pass
4. **Build Project**: Run `./gradlew assembleDebug` to verify compilation
5. **Launch App**: Install and run on emulator/device

## Dependencies

- **AndroidX Libraries**: Core, AppCompat, Material Design
- **RecyclerView**: For efficient list displays
- **CodeView**: Syntax highlighting for Solidity code
- **Gson**: JSON parsing for lesson data
- **Lifecycle Components**: ViewModel and LiveData

## App Usage Guide

### First Launch
1. **Welcome Screen**: The app opens to the main level selection screen
2. **Level 1 Available**: Only the first level (ERC-20 Token Contract) is unlocked initially
3. **Progress Tracking**: Tap the floating action button to view overall progress

### Learning Flow
1. **Select Level**: Tap on an available level card
2. **View Lessons**: Browse through code lessons with syntax highlighting
3. **Read Explanations**: Each lesson includes detailed explanations
4. **Take Quiz**: Complete the quiz to test your understanding
5. **Progress**: Achieve 70% or higher to pass and unlock the next lesson

### Quiz Types
- **Multiple Choice**: Select the correct answer from 4 options
- **Fill-in-the-Blank**: Complete code snippets by filling in missing parts
- **Code Completion**: Write missing code sections

### Progress System
- **Lesson Completion**: Pass quizzes to mark lessons as complete
- **Level Progress**: Complete all lessons in a level to unlock the next
- **Persistent Storage**: Progress is saved automatically
- **Retry Option**: Failed quizzes can be retaken unlimited times

### Navigation Tips
- **Back Button**: Use device back button or toolbar back arrow
- **Progress FAB**: Quick access to detailed progress from main screen
- **Level Cards**: Color-coded status (Available/Locked/Completed)
- **Lesson Indicators**: Checkmarks show completed lessons

## Troubleshooting

### Common Issues
1. **App Won't Build**: Ensure all dependencies are downloaded and SDK versions match
2. **Gradle Sync Failed**: Check internet connection and try "Sync Project with Gradle Files"
3. **Emulator Issues**: Use API 24+ emulator with sufficient RAM (2GB+)
4. **Code Highlighting Not Working**: Ensure CodeView library is properly included

### Performance Tips
- **Device Requirements**: Android 7.0+ for optimal performance
- **Memory**: Ensure device has sufficient free storage (100MB+)
- **Network**: App works offline after initial installation

## Future Enhancements

### Planned Features
- [ ] Advanced code editing with real-time compilation
- [ ] Achievement system with badges and rewards
- [ ] Social features for sharing progress and competing
- [ ] Support for additional programming languages (Rust, Go)
- [ ] Video tutorials integrated with lessons
- [ ] Advanced analytics and learning insights
- [ ] Offline mode for all content
- [ ] Dark mode theme option
- [ ] Accessibility improvements
- [ ] Multi-language support

### Community Features
- [ ] User-generated content and lessons
- [ ] Discussion forums for each level
- [ ] Mentor system for advanced learners
- [ ] Code review and feedback system

## Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues for bugs and feature requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Based on real Solidity contracts from the workspace
- Inspired by interactive coding education platforms
- Uses OpenZeppelin contract standards for examples
