import java.io.*;
import java.util.*;

public class CacheContents {

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            int callLogsRows = Integer.parseInt(reader.readLine().trim());
            int callLogsColumns = Integer.parseInt(reader.readLine().trim());

            List<int[]> callLogs = new ArrayList<>();

            for (int i = 0; i < callLogsRows; i++) {
                String[] callLogTemp = reader.readLine().trim().split("\\s+");

                if (callLogTemp.length != 2) {
                    throw new IllegalArgumentException("Invalid input format: " + Arrays.toString(callLogTemp));
                }

                int timestamp = Integer.parseInt(callLogTemp[0]);
                int itemId = Integer.parseInt(callLogTemp[1]);

                callLogs.add(new int[]{timestamp, itemId});
            }

            List<Integer> result = cacheContents(callLogs);

            for (int itemId : result) {
                System.out.println(itemId);
            }
        } finally {
            reader.close();
        }
    }

    public static List<Integer> cacheContents(List<int[]> callLogs) {
        Map<Integer, Map<String, Integer>> mainMemory = new HashMap<>();
        Map<Integer, Boolean> cache = new HashMap<>();

        for (int[] callLog : callLogs) {
            int timestamp = callLog[0];
            int itemId = callLog[1];

            if (!mainMemory.containsKey(itemId)) {
                Map<String, Integer> itemInfo = new HashMap<>();
                itemInfo.put("priority", 0);
                itemInfo.put("lastAccessTime", timestamp);
                itemInfo.put("accessCount", 0);
                mainMemory.put(itemId, itemInfo);
            }

            Map<String, Integer> itemInfo = mainMemory.get(itemId);
            int lastAccessTime = itemInfo.get("lastAccessTime");
            int priority = itemInfo.get("priority");
            int accessCount = itemInfo.get("accessCount");

            int timeDiff = timestamp - lastAccessTime;
            priority = Math.max(0, priority - timeDiff);
            priority += 2 * accessCount + 2;

            itemInfo.put("priority", priority);
            itemInfo.put("lastAccessTime", timestamp);
            itemInfo.put("accessCount", accessCount + 1);

            if (priority > 5) {
                cache.put(itemId, true);
            } else if (cache.containsKey(itemId) && priority <= 3) {
                cache.remove(itemId);
            }
        }

        List<Integer> sortedCache = new ArrayList<>(cache.keySet());
        Collections.sort(sortedCache);
        return sortedCache.isEmpty() ? Collections.singletonList(-1) : sortedCache;
    }
}
