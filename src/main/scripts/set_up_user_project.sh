#!/bin/bash

#for unix based systems

# Parse command-line arguments
while getopts "s:t:n:" opt; do
    case "$opt" in
        s) sourceDir="$OPTARG";;
        t) targetParentDir="$OPTARG";;
        n) targetDirName="$OPTARG";;
        *) echo "Usage: $0 -s <source_dir> -t <target_parent_dir> -n <target_dir_name>"; exit 1;;
    esac
done

# Check if the target parent directory exists, and create it if not
if [ ! -d "$targetParentDir" ]; then
    mkdir -p "$targetParentDir"
fi

# Define the full target directory path
targetDir="$targetParentDir/$targetDirName"

# Check if the target directory exists, and create it if not
if [ ! -d "$targetDir" ]; then
    mkdir "$targetDir"
else
    rm -rf "$targetDir"
    mkdir "$targetDir"
    echo "Target directory already exists."
fi

# Copy the contents of the source directory to the target directory
cp -r "$sourceDir"/* "$targetDir"

# Check the copy operation result
if [ $? -eq 0 ]; then
    echo "Template directory copied successfully to $targetDir"
else
    echo "Copy operation failed."
    exit 1
fi
