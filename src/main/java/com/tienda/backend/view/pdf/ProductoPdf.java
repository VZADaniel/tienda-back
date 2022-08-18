package com.tienda.backend.view.pdf;

import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tienda.backend.models.entity.Producto;

public class ProductoPdf {
    private List<Producto> productos;

    /**
     * @param productos
     */
    public ProductoPdf(List<Producto> productos) {
        this.productos = productos;
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Nombre", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Precio", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Stock", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        Locale chile = new Locale("es","CL");
        NumberFormat peso = NumberFormat.getCurrencyInstance(chile);
        for(Producto producto: this.productos){
            table.addCell(String.valueOf(producto.getId()));
            table.addCell(String.valueOf(producto.getNombre()));
            table.addCell(String.valueOf(peso.format(producto.getPrecio())));
            table.addCell(String.valueOf(producto.getStock()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException{
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Lista de Productos", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
         
        document.close();
    }
}
