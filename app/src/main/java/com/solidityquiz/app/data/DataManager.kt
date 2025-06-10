package com.solidityquiz.app.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.solidityquiz.app.R
import com.solidityquiz.app.models.*

class DataManager(private val context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("solidity_quiz_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    fun getAllLevels(): List<Level> {
        return listOf(
            createLevel1(),
            createLevel2(),
            createLevel3(),
            createLevel4(),
            createLevel5()
        ).map { level ->
            level.copy(
                isUnlocked = isLevelUnlocked(level.id),
                isCompleted = isLevelCompleted(level.id),
                progress = getLevelProgress(level.id)
            )
        }
    }
    
    fun getLevelById(levelId: Int): Level? {
        return getAllLevels().find { it.id == levelId }
    }
    
    fun getLessonById(levelId: Int, lessonId: Int): Lesson? {
        return getLevelById(levelId)?.lessons?.find { it.id == lessonId }
    }
    
    private fun createLevel1(): Level {
        val lessons = listOf(
            Lesson(
                id = 1,
                levelId = 1,
                title = "ERC-20 Token Basics",
                description = "Learn the fundamentals of ERC-20 token contracts",
                codeContent = """
                    pragma solidity ^0.8.0;
                    
                    contract Token {
                        string public name;
                        string public symbol;
                        uint256 public totalSupply;
                        
                        mapping(address => uint256) public balanceOf;
                    }
                """.trimIndent(),
                explanation = "This is the basic structure of an ERC-20 token. The contract defines the token's name, symbol, total supply, and a mapping to track balances.",
                quizQuestions = createLevel1Questions()
            ),
            Lesson(
                id = 2,
                levelId = 1,
                title = "Token Transfer Function",
                description = "Implement the transfer functionality",
                codeContent = """
                    function transfer(address _to, uint256 _value) 
                        public returns (bool success) {
                        require(balanceOf[msg.sender] >= _value);
                        balanceOf[msg.sender] -= _value;
                        balanceOf[_to] += _value;
                        emit Transfer(msg.sender, _to, _value);
                        return true;
                    }
                """.trimIndent(),
                explanation = "The transfer function allows users to send tokens to other addresses. It checks the sender has enough balance and updates both accounts.",
                quizQuestions = createLevel1TransferQuestions()
            )
        )
        
        return Level(
            id = 1,
            title = "ERC-20 Token Contract",
            description = "Build your first smart contract - a simple ERC-20 token with JavaScript testing",
            iconResource = R.drawable.ic_token,
            lessons = lessons,
            isUnlocked = true // First level is always unlocked
        )
    }
    
    private fun createLevel2(): Level {
        val lessons = listOf(
            Lesson(
                id = 1,
                levelId = 2,
                title = "ERC-721 NFT Basics",
                description = "Understanding Non-Fungible Tokens",
                codeContent = """
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
                """.trimIndent(),
                explanation = "ERC-721 tokens are unique, non-fungible tokens. Each token has a unique ID and can represent ownership of digital assets. The safeMint function creates new tokens with incremental IDs.",
                quizQuestions = createLevel2Questions()
            ),
            Lesson(
                id = 2,
                levelId = 2,
                title = "NFT Metadata and Testing",
                description = "Adding metadata and writing JavaScript tests",
                codeContent = """
                    // JavaScript test for NFT contract
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
                """.trimIndent(),
                explanation = "Testing NFT contracts involves verifying minting functionality, ownership transfers, and metadata handling. Each test should check specific contract behaviors.",
                quizQuestions = createLevel2TestQuestions()
            )
        )

        return Level(
            id = 2,
            title = "ERC-721 NFT Contract",
            description = "Create unique digital assets with NFT contracts and comprehensive testing",
            iconResource = R.drawable.ic_nft,
            lessons = lessons
        )
    }
    
    private fun createLevel3(): Level {
        val lessons = listOf(
            Lesson(
                id = 1,
                levelId = 3,
                title = "Crowdfunding Contract Basics",
                description = "Understanding crowdfunding mechanics with smart contracts",
                codeContent = """
                    pragma solidity ^0.8.0;

                    import "@openzeppelin/contracts/token/ERC721/ERC721.sol";

                    contract CrowdfundingNFT is ERC721 {
                        address public owner;
                        uint256 public goal;
                        uint256 public deadline;
                        uint256 public totalRaised;
                        uint256 private _tokenIdCounter;

                        mapping(address => uint256) public contributions;

                        constructor(
                            string memory name,
                            string memory symbol,
                            uint256 _goal,
                            uint256 _durationInDays
                        ) ERC721(name, symbol) {
                            owner = msg.sender;
                            goal = _goal;
                            deadline = block.timestamp + (_durationInDays * 1 days);
                        }
                    }
                """.trimIndent(),
                explanation = "This crowdfunding contract combines ERC-721 NFTs with fundraising. Contributors receive unique NFTs as rewards for their participation.",
                quizQuestions = createLevel3BasicQuestions()
            ),
            Lesson(
                id = 2,
                levelId = 3,
                title = "Contribution and NFT Minting",
                description = "Implementing contribution tracking and NFT rewards",
                codeContent = """
                    function contribute() external payable {
                        require(block.timestamp < deadline, "Campaign ended");
                        require(msg.value > 0, "Must send ETH");

                        contributions[msg.sender] += msg.value;
                        totalRaised += msg.value;

                        // Mint NFT reward for contributor
                        uint256 tokenId = _tokenIdCounter;
                        _tokenIdCounter++;
                        _safeMint(msg.sender, tokenId);

                        emit ContributionMade(msg.sender, msg.value, tokenId);
                    }

                    function withdraw() external {
                        require(msg.sender == owner, "Only owner can withdraw");
                        require(block.timestamp >= deadline, "Campaign still active");
                        require(totalRaised >= goal, "Goal not reached");

                        payable(owner).transfer(address(this).balance);
                    }

                    event ContributionMade(address contributor, uint256 amount, uint256 tokenId);
                """.trimIndent(),
                explanation = "The contribute function accepts ETH payments and mints NFTs as rewards. The withdraw function allows the owner to claim funds if the goal is met.",
                quizQuestions = createLevel3ContributeQuestions()
            ),
            Lesson(
                id = 3,
                levelId = 3,
                title = "Testing Crowdfunding Contract",
                description = "Comprehensive testing for crowdfunding functionality",
                codeContent = """
                    const { expect } = require('chai');
                    const { ethers } = require('hardhat');

                    describe('CrowdfundingNFT', function () {
                        let crowdfunding, owner, contributor1, contributor2;
                        const goal = ethers.utils.parseEther('10');
                        const duration = 30; // 30 days

                        beforeEach(async function () {
                            [owner, contributor1, contributor2] = await ethers.getSigners();

                            const CrowdfundingNFT = await ethers.getContractFactory('CrowdfundingNFT');
                            crowdfunding = await CrowdfundingNFT.deploy(
                                'CrowdfundNFT', 'CFNFT', goal, duration
                            );
                        });

                        it('Should accept contributions and mint NFTs', async function () {
                            const contribution = ethers.utils.parseEther('1');

                            await crowdfunding.connect(contributor1).contribute({ value: contribution });

                            expect(await crowdfunding.totalRaised()).to.equal(contribution);
                            expect(await crowdfunding.ownerOf(0)).to.equal(contributor1.address);
                            expect(await crowdfunding.contributions(contributor1.address)).to.equal(contribution);
                        });
                    });
                """.trimIndent(),
                explanation = "Testing verifies that contributions are tracked correctly, NFTs are minted for contributors, and the contract state is updated properly.",
                quizQuestions = createLevel3TestQuestions()
            )
        )

        return Level(
            id = 3,
            title = "Crowdfunding via NFT",
            description = "Build a crowdfunding platform that mints NFTs as rewards",
            iconResource = R.drawable.ic_crowdfunding,
            lessons = lessons
        )
    }
    
    private fun createLevel4(): Level {
        val lessons = listOf(
            Lesson(
                id = 1,
                levelId = 4,
                title = "DAO Structure and Governance Token",
                description = "Building the foundation of a decentralized autonomous organization",
                codeContent = """
                    pragma solidity ^0.8.0;

                    contract SimpleDAO {
                        struct Proposal {
                            uint256 id;
                            string description;
                            uint256 amount;
                            address payable recipient;
                            uint256 votes;
                            uint256 deadline;
                            bool executed;
                            mapping(address => bool) voters;
                        }

                        mapping(address => uint256) public shares;
                        mapping(uint256 => Proposal) public proposals;
                        uint256 public totalShares;
                        uint256 public availableFunds;
                        uint256 public proposalCount;

                        event ProposalCreated(uint256 proposalId, string description, uint256 amount);
                        event Voted(uint256 proposalId, address voter, uint256 shares);
                        event ProposalExecuted(uint256 proposalId, uint256 amount, address recipient);
                    }
                """.trimIndent(),
                explanation = "A DAO uses governance tokens (shares) to allow members to vote on proposals. Each proposal has a description, amount, recipient, and voting deadline.",
                quizQuestions = createLevel4BasicQuestions()
            ),
            Lesson(
                id = 2,
                levelId = 4,
                title = "Proposal Creation and Voting",
                description = "Implementing proposal submission and voting mechanisms",
                codeContent = """
                    function createProposal(
                        string memory description,
                        uint256 amount,
                        address payable recipient
                    ) external {
                        require(shares[msg.sender] > 0, "Must be a member");
                        require(amount <= availableFunds, "Insufficient funds");

                        proposalCount++;
                        Proposal storage proposal = proposals[proposalCount];
                        proposal.id = proposalCount;
                        proposal.description = description;
                        proposal.amount = amount;
                        proposal.recipient = recipient;
                        proposal.deadline = block.timestamp + 7 days;

                        emit ProposalCreated(proposalCount, description, amount);
                    }

                    function vote(uint256 proposalId) external {
                        require(shares[msg.sender] > 0, "Must be a member");
                        Proposal storage proposal = proposals[proposalId];
                        require(block.timestamp < proposal.deadline, "Voting ended");
                        require(!proposal.voters[msg.sender], "Already voted");

                        proposal.voters[msg.sender] = true;
                        proposal.votes += shares[msg.sender];

                        emit Voted(proposalId, msg.sender, shares[msg.sender]);
                    }
                """.trimIndent(),
                explanation = "Members can create proposals and vote using their shares. Each member can only vote once per proposal, and voting power is proportional to shares owned.",
                quizQuestions = createLevel4VotingQuestions()
            ),
            Lesson(
                id = 3,
                levelId = 4,
                title = "Proposal Execution and DAO Testing",
                description = "Executing approved proposals and testing DAO functionality",
                codeContent = """
                    function executeProposal(uint256 proposalId) external {
                        Proposal storage proposal = proposals[proposalId];
                        require(block.timestamp >= proposal.deadline, "Voting still active");
                        require(!proposal.executed, "Already executed");
                        require(proposal.votes > totalShares / 2, "Not enough votes");

                        proposal.executed = true;
                        availableFunds -= proposal.amount;
                        proposal.recipient.transfer(proposal.amount);

                        emit ProposalExecuted(proposalId, proposal.amount, proposal.recipient);
                    }

                    // JavaScript Test Example
                    describe('SimpleDAO', function () {
                        it('Should create and execute proposal', async function () {
                            // Give shares to members
                            await dao.connect(owner).addMember(member1.address, 100);
                            await dao.connect(owner).addMember(member2.address, 200);

                            // Create proposal
                            await dao.connect(member1).createProposal(
                                "Fund development",
                                ethers.utils.parseEther("1"),
                                developer.address
                            );

                            // Vote on proposal
                            await dao.connect(member1).vote(1);
                            await dao.connect(member2).vote(1);

                            // Execute after deadline
                            await ethers.provider.send("evm_increaseTime", [7 * 24 * 60 * 60]);
                            await dao.executeProposal(1);
                        });
                    });
                """.trimIndent(),
                explanation = "Proposals are executed if they receive majority votes (>50% of total shares). The DAO transfers funds to the specified recipient upon successful execution.",
                quizQuestions = createLevel4ExecutionQuestions()
            )
        )

        return Level(
            id = 4,
            title = "DAO Contract",
            description = "Create a decentralized autonomous organization with voting mechanisms",
            iconResource = R.drawable.ic_dao,
            lessons = lessons
        )
    }
    
    private fun createLevel5(): Level {
        val lessons = listOf(
            Lesson(
                id = 1,
                levelId = 5,
                title = "AMM Contract Structure",
                description = "Understanding Automated Market Maker mechanics",
                codeContent = """
                    pragma solidity ^0.8.0;

                    import "./Token.sol";

                    contract AMM {
                        Token public token1;
                        Token public token2;

                        uint256 public reserve1;
                        uint256 public reserve2;
                        uint256 public totalShares;
                        mapping(address => uint256) public shares;

                        uint256 public constant PRECISION = 1000000;

                        constructor(address _token1, address _token2) {
                            token1 = Token(_token1);
                            token2 = Token(_token2);
                        }

                        function addLiquidity(uint256 _amount1, uint256 _amount2) external {
                            require(_amount1 > 0 && _amount2 > 0, "Amounts must be greater than 0");

                            token1.transferFrom(msg.sender, address(this), _amount1);
                            token2.transferFrom(msg.sender, address(this), _amount2);

                            uint256 share;
                            if (totalShares == 0) {
                                share = _amount1 * _amount2;
                            } else {
                                share = (_amount1 * totalShares) / reserve1;
                            }

                            shares[msg.sender] += share;
                            totalShares += share;
                            reserve1 += _amount1;
                            reserve2 += _amount2;
                        }
                    }
                """.trimIndent(),
                explanation = "An AMM allows users to trade tokens through liquidity pools. Liquidity providers deposit tokens and receive shares representing their portion of the pool.",
                quizQuestions = createLevel5BasicQuestions()
            ),
            Lesson(
                id = 2,
                levelId = 5,
                title = "Token Swapping Logic",
                description = "Implementing the core swap functionality using constant product formula",
                codeContent = """
                    function swap(address _tokenIn, uint256 _amountIn) external returns (uint256 amountOut) {
                        require(_tokenIn == address(token1) || _tokenIn == address(token2), "Invalid token");
                        require(_amountIn > 0, "Amount must be greater than 0");

                        bool isToken1 = _tokenIn == address(token1);

                        (Token tokenIn, Token tokenOut, uint256 reserveIn, uint256 reserveOut) = isToken1
                            ? (token1, token2, reserve1, reserve2)
                            : (token2, token1, reserve2, reserve1);

                        tokenIn.transferFrom(msg.sender, address(this), _amountIn);

                        // Calculate output using constant product formula: x * y = k
                        uint256 amountInWithFee = (_amountIn * 997) / 1000; // 0.3% fee
                        amountOut = (reserveOut * amountInWithFee) / (reserveIn + amountInWithFee);

                        tokenOut.transfer(msg.sender, amountOut);

                        // Update reserves
                        if (isToken1) {
                            reserve1 += _amountIn;
                            reserve2 -= amountOut;
                        } else {
                            reserve2 += _amountIn;
                            reserve1 -= amountOut;
                        }

                        emit Swap(msg.sender, _tokenIn, _amountIn, amountOut);
                    }

                    event Swap(address indexed user, address indexed tokenIn, uint256 amountIn, uint256 amountOut);
                """.trimIndent(),
                explanation = "The swap function uses the constant product formula (x * y = k) to calculate exchange rates. A 0.3% fee is charged on each swap.",
                quizQuestions = createLevel5SwapQuestions()
            ),
            Lesson(
                id = 3,
                levelId = 5,
                title = "React Frontend Integration",
                description = "Building a React/Redux frontend to interact with the AMM",
                codeContent = """
                    // React Component for AMM Interface
                    import React, { useState, useEffect } from 'react';
                    import { useDispatch, useSelector } from 'react-redux';
                    import { ethers } from 'ethers';

                    const AMMInterface = () => {
                        const [amountIn, setAmountIn] = useState('');
                        const [amountOut, setAmountOut] = useState('');
                        const [selectedToken, setSelectedToken] = useState('token1');

                        const { contract, reserves } = useSelector(state => state.amm);
                        const dispatch = useDispatch();

                        const calculateOutput = async () => {
                            if (!amountIn || !contract) return;

                            const isToken1 = selectedToken === 'token1';
                            const reserveIn = isToken1 ? reserves.reserve1 : reserves.reserve2;
                            const reserveOut = isToken1 ? reserves.reserve2 : reserves.reserve1;

                            const amountInWithFee = (parseFloat(amountIn) * 997) / 1000;
                            const output = (reserveOut * amountInWithFee) / (reserveIn + amountInWithFee);

                            setAmountOut(output.toFixed(6));
                        };

                        const handleSwap = async () => {
                            try {
                                const tokenAddress = selectedToken === 'token1'
                                    ? contract.token1()
                                    : contract.token2();

                                const tx = await contract.swap(
                                    tokenAddress,
                                    ethers.utils.parseEther(amountIn)
                                );

                                await tx.wait();
                                dispatch({ type: 'SWAP_SUCCESS' });
                            } catch (error) {
                                dispatch({ type: 'SWAP_ERROR', payload: error.message });
                            }
                        };

                        return (
                            <div className="amm-interface">
                                <h2>Token Swap</h2>
                                <input
                                    value={amountIn}
                                    onChange={(e) => setAmountIn(e.target.value)}
                                    onBlur={calculateOutput}
                                    placeholder="Amount to swap"
                                />
                                <select
                                    value={selectedToken}
                                    onChange={(e) => setSelectedToken(e.target.value)}
                                >
                                    <option value="token1">Token 1</option>
                                    <option value="token2">Token 2</option>
                                </select>
                                <div>Output: {amountOut}</div>
                                <button onClick={handleSwap}>Swap Tokens</button>
                            </div>
                        );
                    };
                """.trimIndent(),
                explanation = "The React frontend connects to the AMM contract using ethers.js. Redux manages application state, and users can input amounts to see calculated outputs before swapping.",
                quizQuestions = createLevel5ReactQuestions()
            )
        )

        return Level(
            id = 5,
            title = "AMM Contract & React Frontend",
            description = "Build an automated market maker with React/Redux frontend integration",
            iconResource = R.drawable.ic_amm,
            lessons = lessons
        )
    }

    private fun createLevel1Questions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 1,
                lessonId = 1,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the ERC-20 token contract structure:",
                codeSnippet = """
                    contract Token {
                        string public ___;
                        string public ___;
                        uint256 public ___;
                        mapping(address => uint256) public ___;
                    }
                """.trimIndent(),
                correctAnswer = "name,symbol,totalSupply,balanceOf",
                explanation = "These are the essential state variables for an ERC-20 token contract.",
                blanks = listOf(
                    BlankField(1, "token name", "name", 1),
                    BlankField(2, "token symbol", "symbol", 2),
                    BlankField(3, "total supply", "totalSupply", 3),
                    BlankField(4, "balance mapping", "balanceOf", 4)
                )
            ),
            QuizQuestion(
                id = 2,
                lessonId = 1,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What data type is used for token balances in Solidity?",
                options = listOf("int256", "uint256", "string", "bool"),
                correctAnswer = "uint256",
                explanation = "uint256 is used because token balances should never be negative and can be very large numbers."
            )
        )
    }

    private fun createLevel1TransferQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 3,
                lessonId = 2,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the transfer function:",
                codeSnippet = """
                    function transfer(address _to, uint256 _value) public returns (bool) {
                        require(balanceOf[___] >= ___);
                        balanceOf[msg.sender] -= ___;
                        balanceOf[___] += ___;
                        return true;
                    }
                """.trimIndent(),
                correctAnswer = "msg.sender,_value,_value,_to,_value",
                explanation = "The transfer function checks sender balance, deducts from sender, and adds to recipient.",
                blanks = listOf(
                    BlankField(1, "sender address", "msg.sender", 1),
                    BlankField(2, "transfer amount", "_value", 2),
                    BlankField(3, "deduct amount", "_value", 3),
                    BlankField(4, "recipient address", "_to", 4),
                    BlankField(5, "add amount", "_value", 5)
                )
            )
        )
    }

    private fun createLevel2Questions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 4,
                lessonId = 1,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What makes ERC-721 tokens different from ERC-20 tokens?",
                options = listOf(
                    "They are fungible",
                    "They are non-fungible and unique",
                    "They cannot be transferred",
                    "They don't have metadata"
                ),
                correctAnswer = "They are non-fungible and unique",
                explanation = "ERC-721 tokens are non-fungible, meaning each token is unique and cannot be replaced by another token."
            ),
            QuizQuestion(
                id = 5,
                lessonId = 1,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the NFT minting function:",
                codeSnippet = """
                    function safeMint(address to) public {
                        uint256 tokenId = ___;
                        ___++;
                        _safeMint(___, ___);
                    }
                """.trimIndent(),
                correctAnswer = "_tokenIdCounter,_tokenIdCounter,to,tokenId",
                explanation = "The safeMint function uses a counter for unique token IDs and calls _safeMint with the recipient and token ID.",
                blanks = listOf(
                    BlankField(1, "counter variable", "_tokenIdCounter", 1),
                    BlankField(2, "increment counter", "_tokenIdCounter", 2),
                    BlankField(3, "recipient address", "to", 3),
                    BlankField(4, "token ID", "tokenId", 4)
                )
            )
        )
    }

    private fun createLevel2TestQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 6,
                lessonId = 2,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What function is used to check the owner of an NFT?",
                options = listOf("balanceOf", "ownerOf", "tokenURI", "approve"),
                correctAnswer = "ownerOf",
                explanation = "The ownerOf function returns the address that owns a specific token ID."
            ),
            QuizQuestion(
                id = 7,
                lessonId = 2,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the NFT test:",
                codeSnippet = """
                    it('Should mint NFT correctly', async function () {
                        await nft.___(___.address);
                        expect(await nft.___(0)).to.equal(___.address);
                    });
                """.trimIndent(),
                correctAnswer = "safeMint,addr1,ownerOf,addr1",
                explanation = "The test calls safeMint to create an NFT and checks ownership with ownerOf.",
                blanks = listOf(
                    BlankField(1, "mint function", "safeMint", 1),
                    BlankField(2, "recipient", "addr1", 2),
                    BlankField(3, "owner check function", "ownerOf", 3),
                    BlankField(4, "expected owner", "addr1", 4)
                )
            )
        )
    }

    // Level 3 Question Methods
    private fun createLevel3BasicQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 8,
                lessonId = 1,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What is the main purpose of combining crowdfunding with NFTs?",
                options = listOf(
                    "To make the contract more complex",
                    "To provide unique rewards to contributors",
                    "To increase gas costs",
                    "To prevent contributions"
                ),
                correctAnswer = "To provide unique rewards to contributors",
                explanation = "NFTs serve as unique, collectible rewards for crowdfunding contributors, adding value beyond just supporting the project."
            ),
            QuizQuestion(
                id = 9,
                lessonId = 1,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the crowdfunding constructor:",
                codeSnippet = """
                    constructor(
                        string memory name,
                        string memory symbol,
                        uint256 _goal,
                        uint256 _durationInDays
                    ) ERC721(___, ___) {
                        owner = ___;
                        goal = ___;
                        deadline = block.timestamp + (_durationInDays * ___);
                    }
                """.trimIndent(),
                correctAnswer = "name,symbol,msg.sender,_goal,1 days",
                explanation = "The constructor initializes the ERC721 with name and symbol, sets the owner, goal, and calculates deadline.",
                blanks = listOf(
                    BlankField(1, "token name", "name", 1),
                    BlankField(2, "token symbol", "symbol", 2),
                    BlankField(3, "contract owner", "msg.sender", 3),
                    BlankField(4, "funding goal", "_goal", 4),
                    BlankField(5, "time unit", "1 days", 5)
                )
            )
        )
    }

    private fun createLevel3ContributeQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 10,
                lessonId = 2,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the contribute function:",
                codeSnippet = """
                    function contribute() external payable {
                        require(block.timestamp < ___, "Campaign ended");
                        require(___ > 0, "Must send ETH");

                        contributions[___] += ___;
                        totalRaised += ___;

                        uint256 tokenId = ___;
                        ___++;
                        _safeMint(___, ___);
                    }
                """.trimIndent(),
                correctAnswer = "deadline,msg.value,msg.sender,msg.value,msg.value,_tokenIdCounter,_tokenIdCounter,msg.sender,tokenId",
                explanation = "The contribute function checks deadline and value, updates tracking, and mints an NFT reward.",
                blanks = listOf(
                    BlankField(1, "campaign deadline", "deadline", 1),
                    BlankField(2, "sent value", "msg.value", 2),
                    BlankField(3, "contributor address", "msg.sender", 3),
                    BlankField(4, "contribution amount", "msg.value", 4),
                    BlankField(5, "total raised amount", "msg.value", 5),
                    BlankField(6, "current token ID", "_tokenIdCounter", 6),
                    BlankField(7, "increment counter", "_tokenIdCounter", 7),
                    BlankField(8, "NFT recipient", "msg.sender", 8),
                    BlankField(9, "token ID to mint", "tokenId", 9)
                )
            )
        )
    }

    private fun createLevel3TestQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 11,
                lessonId = 3,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What should be tested in a crowdfunding contract?",
                options = listOf(
                    "Only the contribution function",
                    "Contributions, NFT minting, and goal tracking",
                    "Only the withdrawal function",
                    "Only the deadline functionality"
                ),
                correctAnswer = "Contributions, NFT minting, and goal tracking",
                explanation = "Comprehensive testing should cover all major functionality: accepting contributions, minting NFTs, tracking progress, and handling withdrawals."
            )
        )
    }

    // Level 4 Question Methods
    private fun createLevel4BasicQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 12,
                lessonId = 1,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What determines voting power in a DAO?",
                options = listOf(
                    "The number of proposals created",
                    "The amount of shares/tokens owned",
                    "The time since joining",
                    "The number of votes cast"
                ),
                correctAnswer = "The amount of shares/tokens owned",
                explanation = "In most DAOs, voting power is proportional to the number of governance tokens or shares owned by a member."
            ),
            QuizQuestion(
                id = 13,
                lessonId = 1,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the DAO proposal structure:",
                codeSnippet = """
                    struct Proposal {
                        uint256 id;
                        string ___;
                        uint256 ___;
                        address payable ___;
                        uint256 ___;
                        uint256 ___;
                        bool ___;
                        mapping(address => bool) ___;
                    }
                """.trimIndent(),
                correctAnswer = "description,amount,recipient,votes,deadline,executed,voters",
                explanation = "A proposal contains description, amount, recipient, vote count, deadline, execution status, and voter tracking.",
                blanks = listOf(
                    BlankField(1, "proposal description", "description", 1),
                    BlankField(2, "funding amount", "amount", 2),
                    BlankField(3, "payment recipient", "recipient", 3),
                    BlankField(4, "vote count", "votes", 4),
                    BlankField(5, "voting deadline", "deadline", 5),
                    BlankField(6, "execution status", "executed", 6),
                    BlankField(7, "voter tracking", "voters", 7)
                )
            )
        )
    }

    private fun createLevel4VotingQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 14,
                lessonId = 2,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the voting function:",
                codeSnippet = """
                    function vote(uint256 proposalId) external {
                        require(shares[___] > 0, "Must be a member");
                        Proposal storage proposal = proposals[___];
                        require(block.timestamp < proposal.___, "Voting ended");
                        require(!proposal.___[___], "Already voted");

                        proposal.___[___] = true;
                        proposal.___ += shares[___];
                    }
                """.trimIndent(),
                correctAnswer = "msg.sender,proposalId,deadline,voters,msg.sender,voters,msg.sender,votes,msg.sender",
                explanation = "The vote function checks membership, deadline, and prevents double voting while adding vote weight.",
                blanks = listOf(
                    BlankField(1, "voter address", "msg.sender", 1),
                    BlankField(2, "proposal ID", "proposalId", 2),
                    BlankField(3, "voting deadline", "deadline", 3),
                    BlankField(4, "voter mapping", "voters", 4),
                    BlankField(5, "voter address", "msg.sender", 5),
                    BlankField(6, "voter mapping", "voters", 6),
                    BlankField(7, "voter address", "msg.sender", 7),
                    BlankField(8, "vote count", "votes", 8),
                    BlankField(9, "voter shares", "msg.sender", 9)
                )
            )
        )
    }

    private fun createLevel4ExecutionQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 15,
                lessonId = 3,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What condition must be met to execute a DAO proposal?",
                options = listOf(
                    "Any number of votes",
                    "Majority of total shares (>50%)",
                    "Unanimous approval",
                    "Only the owner's approval"
                ),
                correctAnswer = "Majority of total shares (>50%)",
                explanation = "Most DAOs require a majority vote (more than 50% of total voting power) to execute proposals."
            )
        )
    }

    // Level 5 Question Methods
    private fun createLevel5BasicQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 16,
                lessonId = 1,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What is the main purpose of an Automated Market Maker (AMM)?",
                options = listOf(
                    "To create new tokens",
                    "To provide liquidity for token trading",
                    "To mine cryptocurrency",
                    "To store user data"
                ),
                correctAnswer = "To provide liquidity for token trading",
                explanation = "AMMs use liquidity pools to enable decentralized token trading without traditional order books."
            )
        )
    }

    private fun createLevel5SwapQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 17,
                lessonId = 2,
                type = QuestionType.FILL_IN_THE_BLANK,
                question = "Complete the AMM swap calculation:",
                codeSnippet = """
                    uint256 amountInWithFee = (_amountIn * ___) / 1000;
                    amountOut = (reserveOut * ___) / (reserveIn + ___);
                """.trimIndent(),
                correctAnswer = "997,amountInWithFee,amountInWithFee",
                explanation = "The AMM applies a 0.3% fee (997/1000) and uses the constant product formula for price calculation.",
                blanks = listOf(
                    BlankField(1, "fee multiplier", "997", 1),
                    BlankField(2, "amount with fee", "amountInWithFee", 2),
                    BlankField(3, "amount with fee", "amountInWithFee", 3)
                )
            )
        )
    }

    private fun createLevel5ReactQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 18,
                lessonId = 3,
                type = QuestionType.MULTIPLE_CHOICE,
                question = "What library is used to interact with Ethereum contracts in React?",
                options = listOf("web3.js", "ethers.js", "react-ethereum", "blockchain.js"),
                correctAnswer = "ethers.js",
                explanation = "Ethers.js is a popular library for interacting with Ethereum contracts from JavaScript/React applications."
            )
        )
    }

    // Progress tracking methods
    fun isLevelUnlocked(levelId: Int): Boolean {
        if (levelId == 1) return true // First level always unlocked
        val previousLevelCompleted = isLevelCompleted(levelId - 1)
        return sharedPreferences.getBoolean("level_${levelId}_unlocked", previousLevelCompleted)
    }

    fun isLevelCompleted(levelId: Int): Boolean {
        return sharedPreferences.getBoolean("level_${levelId}_completed", false)
    }

    fun getLevelProgress(levelId: Int): Int {
        return sharedPreferences.getInt("level_${levelId}_progress", 0)
    }

    fun markLessonCompleted(levelId: Int, lessonId: Int) {
        sharedPreferences.edit()
            .putBoolean("lesson_${levelId}_${lessonId}_completed", true)
            .apply()
        updateLevelProgress(levelId)
    }

    private fun updateLevelProgress(levelId: Int) {
        val level = getLevelById(levelId) ?: return
        val completedLessons = level.lessons.count { lesson ->
            sharedPreferences.getBoolean("lesson_${levelId}_${lesson.id}_completed", false)
        }
        val progress = (completedLessons * 100) / level.lessons.size

        sharedPreferences.edit()
            .putInt("level_${levelId}_progress", progress)
            .apply()

        if (progress == 100) {
            sharedPreferences.edit()
                .putBoolean("level_${levelId}_completed", true)
                .putBoolean("level_${levelId + 1}_unlocked", true)
                .apply()
        }
    }
}
