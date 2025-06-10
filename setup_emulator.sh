#!/bin/bash

# Android Emulator Setup Script for VS Code
# This script helps set up and launch Android emulator

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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

# Check if Android SDK is installed
check_android_sdk() {
    print_status "Checking Android SDK installation..."
    
    if [ -z "$ANDROID_HOME" ]; then
        print_error "ANDROID_HOME environment variable is not set"
        print_status "Please set ANDROID_HOME to your Android SDK path"
        print_status "Example: export ANDROID_HOME=\$HOME/Library/Android/sdk"
        exit 1
    fi
    
    if [ ! -d "$ANDROID_HOME" ]; then
        print_error "Android SDK directory not found: $ANDROID_HOME"
        exit 1
    fi
    
    print_success "Android SDK found at: $ANDROID_HOME"
}

# Check if emulator command is available
check_emulator_command() {
    print_status "Checking emulator command..."
    
    if ! command -v emulator >/dev/null 2>&1; then
        print_warning "Emulator command not found in PATH"
        print_status "Adding emulator to PATH..."
        export PATH="$ANDROID_HOME/emulator:$PATH"
        
        if ! command -v emulator >/dev/null 2>&1; then
            print_error "Could not find emulator command"
            print_status "Please ensure Android SDK is properly installed"
            exit 1
        fi
    fi
    
    print_success "Emulator command is available"
}

# List available AVDs
list_avds() {
    print_status "Available Android Virtual Devices:"
    
    avd_list=$(emulator -list-avds 2>/dev/null)
    
    if [ -z "$avd_list" ]; then
        print_warning "No AVDs found"
        print_status "Please create an AVD using Android Studio:"
        print_status "1. Open Android Studio"
        print_status "2. Go to Tools → AVD Manager"
        print_status "3. Click 'Create Virtual Device'"
        print_status "4. Choose device and system image (API 24+)"
        return 1
    fi
    
    echo "$avd_list" | while read -r avd; do
        echo "  - $avd"
    done
    
    return 0
}

# Launch emulator
launch_emulator() {
    local avd_name="$1"
    
    if [ -z "$avd_name" ]; then
        print_status "Please specify an AVD name"
        list_avds
        return 1
    fi
    
    print_status "Launching emulator: $avd_name"
    
    # Check if emulator is already running
    if adb devices | grep -q "emulator"; then
        print_warning "An emulator is already running"
        adb devices
        return 0
    fi
    
    # Launch emulator in background
    emulator -avd "$avd_name" -no-snapshot-load &
    
    print_status "Waiting for emulator to start..."
    
    # Wait for emulator to be ready
    timeout=60
    while [ $timeout -gt 0 ]; do
        if adb shell getprop sys.boot_completed 2>/dev/null | grep -q "1"; then
            print_success "Emulator is ready!"
            return 0
        fi
        sleep 2
        timeout=$((timeout - 2))
        echo -n "."
    done
    
    print_error "Emulator failed to start within 60 seconds"
    return 1
}

# Install app on emulator
install_app() {
    print_status "Building and installing app..."
    
    # Check if emulator is running
    if ! adb devices | grep -q "emulator"; then
        print_error "No emulator is running"
        print_status "Please launch an emulator first"
        return 1
    fi
    
    # Build debug APK
    print_status "Building debug APK..."
    ./gradlew assembleDebug
    
    # Install APK
    print_status "Installing APK on emulator..."
    ./gradlew installDebug
    
    print_success "App installed successfully!"
    print_status "You can now launch the Solidity Quiz App on the emulator"
}

# Create a simple AVD
create_simple_avd() {
    print_status "Creating a simple AVD for testing..."
    
    # Check if avdmanager is available
    if ! command -v avdmanager >/dev/null 2>&1; then
        export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"
    fi
    
    if ! command -v avdmanager >/dev/null 2>&1; then
        print_error "avdmanager command not found"
        print_status "Please create an AVD manually using Android Studio"
        return 1
    fi
    
    # Create AVD with API 30 (adjust as needed)
    avd_name="SolidityQuizApp_AVD"
    
    print_status "Creating AVD: $avd_name"
    echo "no" | avdmanager create avd \
        -n "$avd_name" \
        -k "system-images;android-30;google_apis;x86_64" \
        -d "pixel_4"
    
    if [ $? -eq 0 ]; then
        print_success "AVD created: $avd_name"
        return 0
    else
        print_error "Failed to create AVD"
        return 1
    fi
}

# Main function
main() {
    echo "========================================"
    echo "  Android Emulator Setup for VS Code"
    echo "========================================"
    echo
    
    case "${1:-help}" in
        "check")
            check_android_sdk
            check_emulator_command
            list_avds
            ;;
        "list")
            check_android_sdk
            check_emulator_command
            list_avds
            ;;
        "launch")
            check_android_sdk
            check_emulator_command
            launch_emulator "$2"
            ;;
        "install")
            install_app
            ;;
        "create")
            check_android_sdk
            create_simple_avd
            ;;
        "setup")
            check_android_sdk
            check_emulator_command
            if ! list_avds; then
                print_status "Would you like to create a simple AVD? (y/n)"
                read -r response
                if [ "$response" = "y" ] || [ "$response" = "Y" ]; then
                    create_simple_avd
                fi
            fi
            ;;
        "help"|*)
            echo "Usage: $0 [command] [avd_name]"
            echo
            echo "Commands:"
            echo "  check         - Check Android SDK and emulator setup"
            echo "  list          - List available AVDs"
            echo "  launch <avd>  - Launch specific AVD"
            echo "  install       - Build and install app on running emulator"
            echo "  create        - Create a simple AVD for testing"
            echo "  setup         - Complete setup check and AVD creation"
            echo "  help          - Show this help message"
            echo
            echo "Examples:"
            echo "  $0 setup                    # Check setup and create AVD if needed"
            echo "  $0 list                     # List available AVDs"
            echo "  $0 launch Pixel_4_API_30    # Launch specific emulator"
            echo "  $0 install                  # Install app on running emulator"
            ;;
    esac
}

main "$@"
