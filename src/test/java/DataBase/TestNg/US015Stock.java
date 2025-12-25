package DataBase.TestNg;

import DataBase.utilites.DB_Utils;
import DataBase.utilites.TestNgBase;
import org.testng.annotations.Test;

import java.util.List;

public class US015Stock extends TestNgBase {

    @Test
    public void TC015()  {
        //<!-- ==  todo products tablosunda stokta kalmamıs urunleri listeleyen sorguyu yazınız.
        // (Null olan degerler satısa sunulmayan urunlerdir işleme almayınız.)
        // tableName = products
        //  Admin kullanicisi
        //  "products" tablosunda
        //  "unlimited_inventory" sutunu
        //  ve "inventory" sutunlarındaki
        //  degerleri birlestirerek
        //  stockda olmayan urunleri listeler yada
        //  consola "stockda bulunmayam urun yok" yazdırır

        String tableName = "products";
        String unlimitedInvColumn = "unlimited_inventory";
        String invColumn = "inventory";
        String notFoundMessage = "Stokta bulunmayan ürün yok.";

        String query = "SELECT * FROM " + tableName + " WHERE " + unlimitedInvColumn + " = 0 AND " + invColumn + " = 0";
        List<List<Object>> outOfStockProducts = DB_Utils.getQueryResultList(query);

        if (outOfStockProducts.isEmpty()) {
            System.out.println(notFoundMessage);
        } else {
            System.out.println("--- Stokta Olmayan Ürünler ---");
            for (List<Object> product : outOfStockProducts) {
                System.out.println(product);
            }
            System.out.println("-----------------------------");
        }
    }
}
