#include <stdio.h>
#include <stdlib.h>

void bubbleSortAscending(int arr[], int n)
{
    int i, j, temp;
    for (i = 0; i < n - 1; i++)
    {
        for (j = 0; j < n - i - 1; j++)
        {
            if (arr[j] > arr[j + 1])
            {
                // Swap arr[j] and arr[j+1]
                temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

int main()
{
    FILE *file = fopen("/Users/purandhar/Work/Projects/SortingApplication/src/main/resources/static/randomNumbers.csv", "r");
    if (!file)
    {
        perror("Error opening file");
        return 1;
    }

    int *arr = malloc(10000 * sizeof(int)); // Allocate memory
    if (arr == NULL)
    {
        perror("Memory allocation failed");
        fclose(file);
        return 1;
    }

    int n = 0;
    while (fscanf(file, "%d,", &arr[n]) != EOF)
    {
        n++;
        if (n % 10000 == 0)
        {
            // Increase memory allocation if needed
            arr = realloc(arr, (n + 10000) * sizeof(int));
            if (arr == NULL)
            {
                perror("Memory reallocation failed");
                fclose(file);
                return 1;
            }
        }
    }
    fclose(file);

    bubbleSortAscending(arr, n);

    // Optionally, you can print the sorted array to verify correctness
    // for (int i = 0; i < n; i++) {
    //     printf("%d ", arr[i]);
    // }
    // printf("\n");

    free(arr);
    return 0;
}