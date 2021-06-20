package be.winagent.weba2.services.implementation;


import be.winagent.weba2.config.ApplicationConfig;
import be.winagent.weba2.domain.models.Event;
import be.winagent.weba2.domain.models.Location;
import be.winagent.weba2.domain.models.Table;
import be.winagent.weba2.domain.repositories.EventRepository;
import be.winagent.weba2.services.EventService;
import lombok.AllArgsConstructor;
import net.glxn.qrgen.javase.QRCode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventServiceImplementation implements EventService {
    private final EventRepository eventRepository;
    private final ApplicationConfig applicationConfig;

    @Override
    public Optional<Event> find(UUID id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event update(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Optional<Event> getCurrentEvent(Location location) {
        return eventRepository.getEventByLocationAndTime(location, ZonedDateTime.now());
    }

    @Override
    public Page<Event> getFutureEvents(Pageable pageable, Location location) {
        return eventRepository.getFutureEventsForLocation(pageable, location);
    }

    @Override
    public Page<Event> getPastEvents(Pageable pageable,Location location) {
        return eventRepository.getPastEventsForLocation(pageable, location);
    }

    @Override
    public ByteArrayInputStream generateTablePdf(Event event) throws IOException {
        int fontSize = 24;
        PDFont font = PDType1Font.HELVETICA_BOLD;
        int marginBottom = 30;
        int qrCodeSize = 500;

        PDDocument document = new PDDocument();
        for (Table table : event.getTables()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            String title = table.getName();
            float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
            contentStream.setFont(font, fontSize);
            contentStream.beginText();
            contentStream.newLineAtOffset((page.getMediaBox().getWidth() - titleWidth) / 2, marginBottom);
            contentStream.showText(title);
            contentStream.endText();

            float pageWitdh = page.getMediaBox().getUpperRightX() - page.getMediaBox().getLowerLeftX();
            float pageHeight = page.getMediaBox().getUpperRightY() - page.getMediaBox().getLowerLeftY();
            ByteArrayOutputStream img = QRCode
                    .from(
                            String.format("%s/orders/create?table=%s", applicationConfig.getBaseUrl(), table.getId())
                    )
                    .withSize(qrCodeSize, qrCodeSize)
                    .stream();
            PDImageXObject image = PDImageXObject.createFromByteArray(
                    document,
                    img.toByteArray(),
                    String.format("QRcode table %s", table.getName())
            );
            contentStream.drawImage(image, (pageWitdh - qrCodeSize)/2, (pageHeight - qrCodeSize)/2);
            contentStream.close();
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.save(out);
        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}
