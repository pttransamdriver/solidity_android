# Solidity Learning App - Demo Guide

This guide demonstrates how to use the Solidity Learning Android app and showcases its key features.

## App Overview

The Solidity Learning App is designed to teach smart contract development through interactive lessons and quizzes. It features 5 progressive levels covering essential blockchain development concepts.

## Getting Started

### 1. Main Screen
When you launch the app, you'll see the main level selection screen:

- **Welcome Message**: "Welcome to Solidity Learning"
- **Level Cards**: 5 learning levels displayed as cards
- **Progress Indicators**: Visual progress bars for each level
- **Status Labels**: Shows if levels are Available, Locked, or Completed
- **Progress FAB**: Floating action button to view detailed progress

### 2. Level Progression
- **Level 1**: Always unlocked (ERC-20 Token Contract)
- **Levels 2-5**: Unlock as you complete previous levels
- **Progress Tracking**: Each level shows completion percentage

## Level 1: ERC-20 Token Contract

### Lesson 1: ERC-20 Token Basics
**What you'll learn:**
- Basic token contract structure
- State variables (name, symbol, totalSupply, balanceOf)
- Mapping for balance tracking

**Code Example:**
```solidity
pragma solidity ^0.8.0;

contract Token {
    string public name;
    string public symbol;
    uint256 public totalSupply;
    
    mapping(address => uint256) public balanceOf;
}
```

**Quiz Questions:**
1. **Fill-in-the-Blank**: Complete the contract structure
2. **Multiple Choice**: What data type is used for token balances?

### Lesson 2: Token Transfer Function
**What you'll learn:**
- Transfer function implementation
- Balance checking with require statements
- Event emission

**Code Example:**
```solidity
function transfer(address _to, uint256 _value) 
    public returns (bool success) {
    require(balanceOf[msg.sender] >= _value);
    balanceOf[msg.sender] -= _value;
    balanceOf[_to] += _value;
    emit Transfer(msg.sender, _to, _value);
    return true;
}
```

**Quiz Questions:**
1. **Fill-in-the-Blank**: Complete the transfer function
2. **Multiple Choice**: Understanding of require statements

## Level 2: ERC-721 NFT Contract

### Lesson 1: ERC-721 NFT Basics
**What you'll learn:**
- Non-fungible token concepts
- OpenZeppelin imports
- Token ID management
- Minting functions

**Code Example:**
```solidity
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC721/ERC721.sol";

contract MyNFT is ERC721 {
    uint256 private _tokenIdCounter;
    
    constructor() ERC721("MyNFT", "MNFT") {}
    
    function safeMint(address to) public {
        uint256 tokenId = _tokenIdCounter;
        _tokenIdCounter++;
        _safeMint(to, tokenId);
    }
}
```

### Lesson 2: NFT Metadata and Testing
**What you'll learn:**
- JavaScript testing with Hardhat
- NFT ownership verification
- Test structure and assertions

**Code Example:**
```javascript
const { expect } = require('chai');
const { ethers } = require('hardhat');

describe('MyNFT', function () {
    let nft, owner, addr1;
    
    beforeEach(async function () {
        [owner, addr1] = await ethers.getSigners();
        const MyNFT = await ethers.getContractFactory('MyNFT');
        nft = await MyNFT.deploy();
    });
    
    it('Should mint NFT correctly', async function () {
        await nft.safeMint(addr1.address);
        expect(await nft.ownerOf(0)).to.equal(addr1.address);
    });
});
```

## App Features Demonstration

### 1. Interactive Code Display
- **Syntax Highlighting**: Solidity code with proper color coding
- **Scrollable Content**: Long code examples in scrollable views
- **Explanation Text**: Detailed explanations below each code block

### 2. Quiz System
- **Multiple Question Types**:
  - Multiple Choice: Select from 4 options
  - Fill-in-the-Blank: Complete code snippets
  - Code Completion: Write missing code sections

- **Immediate Feedback**: 
  - Correct answers show green feedback
  - Incorrect answers show explanation
  - Progress to next question after submission

### 3. Progress Tracking
- **Level Progress**: Percentage completion for each level
- **Lesson Completion**: Checkmarks for completed lessons
- **Overall Statistics**: Total progress across all levels
- **Persistent Storage**: Progress saved between app sessions

### 4. User Interface
- **Material Design**: Modern Android UI components
- **Card-based Layout**: Clean, organized content presentation
- **Responsive Design**: Works on different screen sizes
- **Intuitive Navigation**: Easy movement between screens

## Navigation Flow

1. **Main Screen** → Select a level
2. **Lesson Screen** → View code and explanations
3. **Quiz Screen** → Answer questions
4. **Progress Screen** → View detailed progress
5. **Return to Main** → Continue with next level

## Scoring System

- **Quiz Passing**: 70% or higher to complete a lesson
- **Level Completion**: All lessons must be completed
- **Unlock System**: Complete current level to unlock next
- **Retry Option**: Failed quizzes can be retaken

## Future Levels (Planned)

### Level 3: Crowdfunding via NFT
- Crowdfunding contract mechanics
- NFT reward systems
- Goal tracking and fund distribution

### Level 4: DAO Contract
- Governance token implementation
- Proposal creation and voting
- Execution mechanisms

### Level 5: AMM Contract & React Frontend
- Automated Market Maker logic
- Liquidity pool management
- React/Redux integration

## Tips for Best Learning Experience

1. **Read Carefully**: Take time to understand code explanations
2. **Practice**: Try to understand each line of code
3. **Take Notes**: Write down key concepts
4. **Retry Quizzes**: Don't hesitate to retake failed quizzes
5. **Progress Gradually**: Complete levels in order for best understanding

## Technical Requirements

- **Android Version**: 7.0 (API 24) or higher
- **Storage**: ~50MB for app installation
- **Internet**: Not required (offline learning)
- **Screen Size**: Optimized for phones and tablets

This demo guide provides a comprehensive overview of the Solidity Learning App's features and functionality. The app is designed to make blockchain development accessible through interactive, hands-on learning.
