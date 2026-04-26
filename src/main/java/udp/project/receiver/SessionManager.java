    package udp.project.receiver;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import udp.project.protocol.Packet;
    import udp.project.protocol.PacketType;
    import udp.project.utils.Md5Util;

    import java.util.*;

    public class SessionManager {

        private static final Logger log = LoggerFactory.getLogger(SessionManager.class);

        // laufende Übertragungen txId -> Session
        private final Map<Integer, TransferSession> sessions = new HashMap<>();

        // Pakete, die vor FIRST angekommen sind
        private final Map<Integer, List<Packet>> pending = new HashMap<>();

        // Datei am Ende zusammensetzen
        private final FileAssembler assembler = new FileAssembler();

        // wird für jedes ankommende Paket aufgerufen
        public void handle(Packet packet) {

            int id = packet.getTransmissionId();

            log.debug("Incoming packet: type={}, seq={}, txId={}",
                    packet.getType(),
                    packet.getSequenceNumber(),
                    id
            );

            if (packet.getType() == PacketType.FIRST) {
                handleFirst(packet);
                return;
            }

            TransferSession session = sessions.get(id);

            if (session == null) {
                log.warn("Packet before FIRST → buffering (txId={}, seq={})",
                        id,
                        packet.getSequenceNumber()
                );
                pending.computeIfAbsent(id, k -> new ArrayList<>()).add(packet);
                return;
            }

            session.accept(packet);

            log.debug("Progress: {}/{} packets",
                    session.getChunks().size(),
                    session.getMaxSeq()
            );

            checkCompletion(session);
        }

        // FIRST Paket -> Session erstellen
        private void handleFirst(Packet packet) {

            int id = packet.getTransmissionId();

            log.info("New transfer started (txId={})", id);
            log.info("File: '{}' (expected {} packets)",
                    packet.getFileName(),
                    packet.getMaxSequenceNumber()
            );

            TransferSession session = new TransferSession(id);
            sessions.put(id, session);

            session.accept(packet);

            List<Packet> buffered = pending.remove(id);
            if (buffered != null) {
                log.info("Applying {} buffered packets", buffered.size());

                for (Packet p : buffered) {
                    session.accept(p);
                }
            }

            checkCompletion(session);
        }

        // wenn alles da ist -> Datei bauen + prüfen
        private void checkCompletion(TransferSession session) {

            if (!session.isComplete()) return;

            String fileName = "received_" + session.getFileName();

            log.info("All packets received for '{}'", session.getFileName());
            log.info("Starting file assembly...");

            try {
                assembler.buildFile(
                        session.getChunks(),
                        fileName,
                        session.getMaxSeq()
                );

                log.info("File created: {}", fileName);

                byte[] actualMd5 = Md5Util.calculateFile(fileName);

                if (Arrays.equals(actualMd5, session.getExpectedMd5())) {
                    log.info("MD5 check OK");
                } else {
                    log.error("MD5 mismatch");
                }

            } catch (Exception e) {
                log.error("Error during file assembly", e);
            }

            sessions.remove(session.getTransmissionId());
        }
    }