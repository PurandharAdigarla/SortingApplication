public class SortTech {
    //bubble sorting swapping
    void swapBubbleAc(long[] arr, int j) {
        if (arr[j] > arr[j + 1]) {
            long temp = arr[j];
            arr[j] = arr[j + 1];
            arr[j + 1] = temp;
        }
    }

    void swapBubbleDc(long[] arr, int j) {
        if (arr[j] < arr[j + 1]) {
            long temp = arr[j];
            arr[j] = arr[j + 1];
            arr[j + 1] = temp;
        }
    }

    //ascending sorting
    void bubbleSortAccenting(long[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                swapBubbleAc(arr, j);
            }
        }
    }

    //descending sorting
    void bubbleSortDescending(long[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                swapBubbleDc(arr, j);
            }
        }
    }
}
