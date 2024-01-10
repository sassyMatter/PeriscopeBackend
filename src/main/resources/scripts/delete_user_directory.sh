#!/bin/bash

# Parse command-line arguments
while getopts "d:" opt; do
    case "$opt" in
        d) directoryToDelete="$OPTARG";;
        *) echo "Usage: $0 -d <directory_to_delete>"; exit 1;;
    esac
done

# Check if the directory exists before deleting
if [ -d "$directoryToDelete" ]; then
    # Delete the directory
    rm -rf "$directoryToDelete"
    echo "Deleted directory: $directoryToDelete"
else
    echo "Directory does not exist: $directoryToDelete"
    exit 1
fi
