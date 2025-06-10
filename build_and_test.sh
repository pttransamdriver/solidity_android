#!/bin/bash

# Solidity Quiz App - Build and Test Script
# This script automates the build, test, and deployment process

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to check if command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    if ! command_exists java; then
        print_error "Java is not installed. Please install JDK 11 or higher."
        exit 1
    fi
    
    if ! command_exists ./gradlew; then
        print_error "Gradle wrapper not found. Make sure you're in the project root directory."
        exit 1
    fi
    
    # Check Java version
    java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$java_version" -lt 11 ]; then
        print_warning "Java version is $java_version. JDK 11+ is recommended."
    fi
    
    print_success "Prerequisites check completed"
}

# Clean project
clean_project() {
    print_status "Cleaning project..."
    ./gradlew clean
    print_success "Project cleaned"
}

# Run unit tests
run_tests() {
    print_status "Running unit tests..."
    ./gradlew test
    
    if [ $? -eq 0 ]; then
        print_success "All unit tests passed"
    else
        print_error "Some unit tests failed"
        exit 1
    fi
}

# Build debug APK
build_debug() {
    print_status "Building debug APK..."
    ./gradlew assembleDebug
    
    if [ $? -eq 0 ]; then
        print_success "Debug APK built successfully"
        print_status "APK location: app/build/outputs/apk/debug/app-debug.apk"
    else
        print_error "Debug build failed"
        exit 1
    fi
}

# Build release APK
build_release() {
    print_status "Building release APK..."
    ./gradlew assembleRelease
    
    if [ $? -eq 0 ]; then
        print_success "Release APK built successfully"
        print_status "APK location: app/build/outputs/apk/release/app-release-unsigned.apk"
    else
        print_error "Release build failed"
        exit 1
    fi
}

# Install on connected device
install_debug() {
    print_status "Installing debug APK on connected device..."
    
    # Check if device is connected
    if command_exists adb; then
        device_count=$(adb devices | grep -c "device$")
        if [ "$device_count" -eq 0 ]; then
            print_warning "No Android devices connected"
            return 1
        fi
    fi
    
    ./gradlew installDebug
    
    if [ $? -eq 0 ]; then
        print_success "App installed successfully"
    else
        print_error "Installation failed"
        exit 1
    fi
}

# Generate test coverage report
generate_coverage() {
    print_status "Generating test coverage report..."
    ./gradlew jacocoTestReport
    
    if [ $? -eq 0 ]; then
        print_success "Coverage report generated"
        print_status "Report location: app/build/reports/jacoco/jacocoTestReport/html/index.html"
    else
        print_warning "Coverage report generation failed"
    fi
}

# Lint check
run_lint() {
    print_status "Running lint checks..."
    ./gradlew lint
    
    if [ $? -eq 0 ]; then
        print_success "Lint checks passed"
    else
        print_warning "Lint checks found issues"
        print_status "Lint report: app/build/reports/lint-results.html"
    fi
}

# Main execution
main() {
    echo "======================================"
    echo "  Solidity Quiz App Build Script"
    echo "======================================"
    echo
    
    # Parse command line arguments
    case "${1:-all}" in
        "clean")
            check_prerequisites
            clean_project
            ;;
        "test")
            check_prerequisites
            run_tests
            ;;
        "debug")
            check_prerequisites
            clean_project
            run_tests
            build_debug
            ;;
        "release")
            check_prerequisites
            clean_project
            run_tests
            run_lint
            build_release
            ;;
        "install")
            check_prerequisites
            build_debug
            install_debug
            ;;
        "coverage")
            check_prerequisites
            run_tests
            generate_coverage
            ;;
        "lint")
            check_prerequisites
            run_lint
            ;;
        "all")
            check_prerequisites
            clean_project
            run_tests
            run_lint
            build_debug
            generate_coverage
            ;;
        "help"|"-h"|"--help")
            echo "Usage: $0 [command]"
            echo
            echo "Commands:"
            echo "  clean     - Clean the project"
            echo "  test      - Run unit tests"
            echo "  debug     - Build debug APK"
            echo "  release   - Build release APK"
            echo "  install   - Build and install debug APK"
            echo "  coverage  - Generate test coverage report"
            echo "  lint      - Run lint checks"
            echo "  all       - Run all checks and build debug (default)"
            echo "  help      - Show this help message"
            echo
            exit 0
            ;;
        *)
            print_error "Unknown command: $1"
            echo "Use '$0 help' for usage information"
            exit 1
            ;;
    esac
    
    echo
    print_success "Build script completed successfully!"
}

# Run main function with all arguments
main "$@"
