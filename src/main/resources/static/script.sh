#!/bin/bash

# Define paths
JAVA_DIR="/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/java"
C_DIR="/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/c"
RANDOM_NUMBERS_FILE="/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumbers.csv"

# Get the sorting choice from command line argument
choice=$1

# Ensure the random numbers file exists
if [ ! -f "$RANDOM_NUMBERS_FILE" ]; then
    echo "Random numbers file does not exist. Please generate random numbers first."
    exit 1
fi

measure_time() {
    local command="$@"
    local elapsed_time
    # Run the command and measure time in milliseconds
    elapsed_time=$( { /usr/bin/time -p bash -c "$command" 2>&1; } | grep '^real' | awk '{print $2 * 1000}' )
    echo "$elapsed_time"
}

# Perform sorting based on the choice
case $choice in
    ascending)
        echo "Performing ascending sorting..."

        # Compile and run Java sorting
        cd $JAVA_DIR || { echo "Failed to change directory to $JAVA_DIR"; exit 1; }
        javac JavaBubbleSortingAssenting.java || { echo "Failed to compile JavaBubbleSortingAssenting.java"; exit 1; }
        java_time=$(measure_time java JavaBubbleSortingAssenting $RANDOM_NUMBERS_FILE)

        # Compile and run C sorting
        cd $C_DIR || { echo "Failed to change directory to $C_DIR"; exit 1; }
        gcc -o bubble_sort_ascending bubble_sort_ascending.c || { echo "Failed to compile bubble_sort_ascending.c"; exit 1; }
        if [ -f ./bubble_sort_ascending ]; then
            c_time=$(measure_time ./bubble_sort_ascending $RANDOM_NUMBERS_FILE)
        else
            echo "C sorting binary not found."
            c_time="N/A"
        fi

        ;;
    descending)
        echo "Performing descending sorting..."

        # Compile and run Java sorting
        cd $JAVA_DIR || { echo "Failed to change directory to $JAVA_DIR"; exit 1; }
        javac JavaBubbleSortingDescent.java || { echo "Failed to compile JavaBubbleSortingDescent.java"; exit 1; }
        java_time=$(measure_time java JavaBubbleSortingDescent $RANDOM_NUMBERS_FILE)

        # Compile and run C sorting
        cd $C_DIR || { echo "Failed to change directory to $C_DIR"; exit 1; }
        gcc -o bubble_sort_descending bubble_sort_descending.c || { echo "Failed to compile bubble_sort_descending.c"; exit 1; }
        if [ -f ./bubble_sort_descending ]; then
            c_time=$(measure_time ./bubble_sort_descending $RANDOM_NUMBERS_FILE)
        else
            echo "C sorting binary not found."
            c_time="N/A"
        fi

        ;;
    *)
        echo "Invalid choice!"
        exit 1
        ;;
esac

# Print results
echo "Java sorting time: $java_time milliseconds"
echo "C sorting time: $c_time milliseconds"

echo "Sorting complete."