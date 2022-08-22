package com.tienda.backend.view.pdf;

import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
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
import com.tienda.backend.models.dtos.ClienteDto;
import com.tienda.backend.models.dtos.EnvioDto;
import com.tienda.backend.models.dtos.ItemCartDto;

public class DetalleVentaPdf {
    private ClienteDto cliente;
    private EnvioDto envio;
    private List<ItemCartDto> productos;

    /**
     * @param cliente
     * @param envio
     * @param productos
     */
    public DetalleVentaPdf(ClienteDto cliente, EnvioDto envio, List<ItemCartDto> productos) {
        this.cliente = cliente;
        this.envio = envio;
        this.productos = productos;
    }

    private void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("CÓDIGO", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("NOMBRE", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("PRECIO UNITARIO", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("CANTIDAD", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("TOTAL", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        Locale chile = new Locale("es","CL");
        NumberFormat peso = NumberFormat.getCurrencyInstance(chile);
        for(ItemCartDto item: this.productos){
            table.addCell(String.valueOf(item.getCodigo()));
            table.addCell(String.valueOf(item.getNombre()));
            table.addCell(String.valueOf(peso.format(item.getPrecio())));
            table.addCell(String.valueOf(item.getCantidad()));
            table.addCell(String.valueOf(peso.format(item.getTotal())));
        }
    }

    public Document export(HttpServletResponse response) throws DocumentException, IOException{
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Pedido "+cliente.getNombre().concat(" ").concat(cliente.getApellido()), font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.0f, 3.0f, 2.0f, 3.0f,3.0f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

        return document;
    }
}
