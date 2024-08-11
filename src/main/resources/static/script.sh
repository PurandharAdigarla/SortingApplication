#!/bin/bash

# Define paths
JAVA_DIR="/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/java"

# Get the sorting choice from command line argument
choice=$1

# Call the random number generation script
echo "Calling random number generation script..."
/bin/bash /Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumber.sh

case $choice in
    ascending)
        echo "Performing ascending sorting..."
        cd $JAVA_DIR
        javac JavaBubbleSortingAssenting.java
        java JavaBubbleSortingAssenting
        ;;
    descending)
        echo "Performing descending sorting..."
        cd $JAVA_DIR
        javac JavaBubbleSortingDescent.java
        java JavaBubbleSortingDescent
        ;;
    *)
        echo "Invalid choice!"
        exit 1
        ;;
esac

echo "Sorting complete."