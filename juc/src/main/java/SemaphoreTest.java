import java.sql.Time;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author CodingXXM
 * @desc
 * @date 2019/10/22 19:51
 **/
public class SemaphoreTest {
    public static void main(String[] args) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(2);
        semaphore.acquire();
        semaphore.release();
    }
}

