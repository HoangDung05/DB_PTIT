package db_ptit;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.Map;

import RMI.ByteService;

public class RMI_Byte {
    // Sửa lỗi 1: Thêm throws Exception để xử lý lỗi mạng RMI
    public static void main(String[] args) throws Exception {
        
        Registry registry = LocateRegistry.getRegistry("36.50.135.242", 1099);
        ByteService service = (ByteService) registry.lookup("RMIByteService");

        byte[] data = service.requestData("B23DCCN199", "Q36hWul3");

        LinkedHashMap countMap = new LinkedHashMap<>();
        for (byte b : data) {
            countMap.put(b, countMap.getOrDefault(b, 0) + 1);
        }

        byte minByte = 0;
        int minCount = Integer.MAX_VALUE;

        // Sửa lỗi 2: Thêm  để Java hiểu đúng kiểu dữ liệu
        for (Map.Entry entry : countMap.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                minByte = entry.getKey();
            }
        }

        byte[] result = new byte[] { minByte, (byte) minCount };
        System.out.println("Phần tử ít nhất: " + minByte + ", số lần: " + minCount);

        service.submitData("B23DCCN199", "Q36hWul3", result);
        System.out.println("Giao tiếp RMI thành công!");
    }
}