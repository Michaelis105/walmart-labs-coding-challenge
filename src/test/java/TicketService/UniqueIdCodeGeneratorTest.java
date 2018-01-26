package TicketService;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class UniqueIdCodeGeneratorTest {

    Set<String> s;
    boolean foundDuplicate;
    ConfirmationCodeGenerator ccg;

    @Test
    public void GenerateUniqueIdsMultithread() {
        s = new HashSet<>();
        ccg = new ConfirmationCodeGenerator();
        foundDuplicate = false;
        for (int i = 0; i < 1000; i++) {
            GenerateIDThread r = new GenerateIDThread();
            Thread t = new Thread(r);
            t.start();
        }
        Assert.assertFalse(foundDuplicate);
    }


    class GenerateIDThread implements Runnable {
        private String id;

        @Override
        public void run() {
            try {
                id = ccg.generateId();
                synchronized (s) {
                    if (s.contains(id)) foundDuplicate = true;
                    s.add(id);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getUniqueId() { return id; }
    }
}
