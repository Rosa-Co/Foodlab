package progetto.app.dialog;

import atlantafx.base.theme.Styles;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TermsOfServiceDialog extends Dialog<ButtonType> {

    public TermsOfServiceDialog() {
        setTitle("Termini e Condizioni del Servizio");
        setHeaderText("Unina FoodLab - Terms of Service");

        // Bottoni
        getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        // Contenuto
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Scroll pane per il testo
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox textContainer = new VBox(15);
        textContainer.setPadding(new Insets(10));

        // Testo dei ToS
        addSection(textContainer, "1. Accettazione dei Termini",
                "Utilizzando Unina FoodLab, accetti questi termini di servizio. Se non accetti questi termini, " +
                        "non potrai utilizzare la piattaforma. Ci riserviamo il diritto di modificare questi termini " +
                        "in qualsiasi momento, con notifica preventiva agli utenti registrati.");

        addSection(textContainer, "2. Descrizione del Servizio",
                "Unina FoodLab è una piattaforma gratuita dedicata alla condivisione di corsi di cucina tra chef professionisti " +
                        "e appassionati di gastronomia. Gli chef possono creare, pubblicare e gestire corsi culinari, " +
                        "mentre gli utenti possono iscriversi, partecipare e interagire con i contenuti offerti. " +
                        "Il servizio è completamente gratuito e non prevede alcun pagamento.");

        addSection(textContainer, "3. Account Utente",
                "Per utilizzare Unina FoodLab devi creare un account fornendo informazioni accurate e complete. " +
                        "Sei responsabile della sicurezza del tuo account e di tutte le attività svolte attraverso di esso. " +
                        "Non puoi condividere le tue credenziali con terzi. Devi notificarci immediatamente in caso di " +
                        "accesso non autorizzato al tuo account.");

        addSection(textContainer, "4. Account Chef",
                "Gli chef che desiderano pubblicare corsi devono fornire prove della loro qualifica professionale " +
                        "o esperienza nel settore culinario. Unina FoodLab si riserva il diritto di verificare le credenziali " +
                        "e rifiutare o sospendere account che non rispettano gli standard qualitativi della piattaforma.");

        addSection(textContainer, "5. Contenuti e Proprietà Intellettuale",
                "Gli chef mantengono la proprietà intellettuale sui contenuti che pubblicano (ricette, video, materiali didattici). " +
                        "Pubblicando contenuti su Unina FoodLab, concedi alla piattaforma una licenza non esclusiva per " +
                        "distribuire, mostrare e promuovere tali contenuti. Gli utenti non possono copiare, redistribuire " +
                        "o utilizzare commercialmente i contenuti senza autorizzazione esplicita.");

        addSection(textContainer, "6. Condotta degli Utenti",
                "È vietato:\n" +
                        "• Pubblicare contenuti offensivi, discriminatori o inappropriati\n" +
                        "• Utilizzare la piattaforma per attività illegali\n" +
                        "• Molestare, intimidire o minacciare altri utenti\n" +
                        "• Pubblicare spam o contenuti promozionali non autorizzati\n" +
                        "• Tentare di violare la sicurezza della piattaforma\n" +
                        "• Creare account falsi o impersonare altri utenti");

        addSection(textContainer, "7. Gratuità del Servizio",
                "Unina FoodLab è una piattaforma completamente gratuita. Non sono previsti costi di iscrizione, " +
                        "abbonamenti o pagamenti per l'accesso ai corsi. Il servizio è offerto come progetto educativo " +
                        "e non ha finalità commerciali. Ci riserviamo il diritto di mantenere questo modello gratuito o " +
                        "di introdurre eventuali funzionalità premium in futuro, con adeguata comunicazione agli utenti.");

        addSection(textContainer, "8. Privacy e Protezione dei Dati",
                "Unina FoodLab raccoglie e tratta i dati personali in conformità al GDPR e alla normativa italiana sulla privacy. " +
                        "I tuoi dati vengono utilizzati per fornire il servizio, migliorare l'esperienza utente e comunicazioni " +
                        "relative alla piattaforma. Non vendiamo i tuoi dati a terzi. Per maggiori informazioni, consulta la " +
                        "nostra Privacy Policy.");

        addSection(textContainer, "9. Responsabilità e Garanzie",
                "Unina FoodLab fornisce la piattaforma \"così com'è\" senza garanzie implicite. Non siamo responsabili per:\n" +
                        "• La qualità o accuratezza dei contenuti pubblicati dagli chef\n" +
                        "• Danni derivanti dall'utilizzo di ricette o tecniche apprese sulla piattaforma\n" +
                        "• Interruzioni del servizio o perdita di dati\n" +
                        "• Controversie tra chef e utenti\n\n" +
                        "Gli chef sono responsabili della qualità e sicurezza dei contenuti che pubblicano.");

        addSection(textContainer, "10. Risoluzione delle Controversie",
                "In caso di controversie, le parti si impegnano a cercare una soluzione amichevole. " +
                        "Per le controversie non risolte, sarà competente il foro di Napoli. " +
                        "Si applica la legge italiana.");

        addSection(textContainer, "11. Sospensione e Cancellazione",
                "Unina FoodLab si riserva il diritto di sospendere o cancellare account che violano questi termini " +
                        "o le policy della piattaforma. Essendo il servizio gratuito, la cancellazione dell'account " +
                        "non comporta alcun rimborso, ma gli utenti mantengono l'accesso ai contenuti fino al completamento " +
                        "dei corsi in corso.");

        addSection(textContainer, "12. Modifiche al Servizio",
                "Unina FoodLab può modificare, sospendere o interrompere qualsiasi parte del servizio in qualsiasi momento. " +
                        "Faremo del nostro meglio per comunicare con anticipo modifiche significative che potrebbero " +
                        "impattare negativamente l'esperienza degli utenti.");

        addSection(textContainer, "13. Contatti",
                "Per domande su questi termini o sul servizio, contattaci a:\n" +
                        "Email: legal@uninafoodlab.it\n" +
                        "Supporto: support@uninafoodlab.it\n" +
                        "Indirizzo: Università degli Studi di Napoli Federico II, Corso Umberto I, 80138 Napoli, Italia");

        Text lastUpdate = new Text("\n\nUltimo aggiornamento: 24 Novembre 2025");
        lastUpdate.setStyle("-fx-font-style: italic; -fx-opacity: 0.6; -fx-font-size: 11px;");
        textContainer.getChildren().add(lastUpdate);

        scrollPane.setContent(textContainer);
        content.getChildren().add(scrollPane);

        getDialogPane().setContent(content);

        // Stile
        DialogPane dialogPane = getDialogPane();
        dialogPane.getStyleClass().add(Styles.ROUNDED);
        dialogPane.setPrefWidth(700);
        dialogPane.setPrefHeight(550);

        dialogPane.lookupButton(ButtonType.CLOSE).getStyleClass().addAll(
                Styles.ACCENT,
                Styles.ROUNDED
        );
    }

    private void addSection(VBox container, String title, String content) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Text contentText = new Text(content);
        contentText.setStyle("-fx-font-size: 12px;");
        contentText.wrappingWidthProperty().bind(container.widthProperty().subtract(40));

        TextFlow textFlow = new TextFlow(contentText);

        container.getChildren().addAll(titleLabel, textFlow);
    }

    public void showDialog() {
        showAndWait();
    }
}