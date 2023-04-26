package ru.wostarnn.nxbootcampbss.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.wostarnn.nxbootcampbss.dao.AbonentDAO;
import ru.wostarnn.nxbootcampbss.dao.CallDAO;
import ru.wostarnn.nxbootcampbss.dao.TariffDAO;
import ru.wostarnn.nxbootcampbss.entities.Abonent;
import ru.wostarnn.nxbootcampbss.entities.Call;
import ru.wostarnn.nxbootcampbss.entities.Tariff;
import ru.wostarnn.nxbootcampbss.response.ReportResponseBody;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AbonentServiceTest {
    @Mock
    private AbonentDAO abonentDAO;
    @Mock
    private CallDAO callDAO;
    @Mock
    private TariffDAO tariffDAO;
    @InjectMocks
    private AbonentService abonentService;

    @Test
    void pay_getsValidData_returnsAbonent() {
        //Given
        String phoneNumber = "72234125671";
        double payValue = 150.00;
        Abonent abonent = getAbonent(phoneNumber);
        when(abonentDAO.findAbonent(phoneNumber)).thenReturn(abonent);
        //When
        Abonent result = abonentService.pay(phoneNumber, payValue);
        //Then
        assertNotNull(result);
        assertEquals(200.00, result.getBalance());
    }

    @Test
    void generateReport_getsValidData_returnsReportResponseBody() {
        //Given
        String phoneNumber = "79995554433";
        Abonent abonent = getAbonent(phoneNumber);
        List<Call> callList = getCallList(phoneNumber);
        Tariff tariff = getTariff();

        when(abonentDAO.findAbonent(phoneNumber)).thenReturn(abonent);
        when(callDAO.findByPhoneNumber(phoneNumber)).thenReturn(callList);
        when(tariffDAO.findById(abonent.getTariff_id())).thenReturn(tariff);
        //When
        ReportResponseBody responseBody = abonentService.generateReport(phoneNumber);
        //Then
        assertNotNull(responseBody);
        assertEquals(2L, responseBody.getId());
        assertEquals(155.00, responseBody.getTotalCost());
        assertEquals("06", responseBody.getTariffIndex());
        assertNotEquals("26", responseBody.getTariffIndex());
        assertEquals("rubles", responseBody.getMonetaryUnit());
        assertEquals("79995554433", responseBody.getNumberPhone());
        //Придумать реализацию проверки payload
        //assertEquals(callList, responseBody.getPayload());
    }

    private Abonent getAbonent(String phoneNumber) {
        Abonent abonent = new Abonent();
        abonent.setId(2L);
        abonent.setPhoneNumber(phoneNumber);
        abonent.setBalance(50.00);
        abonent.setTariff_id("06");
        return abonent;
    }

    private List<Call> getCallList(String phoneNumber) {
        Call call1 = new Call();
        call1.setId(1L);
        call1.setCost(46.50);
        call1.setDuration("00:30:26");
        call1.setType("02");
        call1.setStartTime("2023-04-09 22:29:02");
        call1.setEndTime("2023-04-09 22:59:28");
        call1.setPhoneNumber(phoneNumber);

        Call call2 = new Call();
        call2.setId(2L);
        call2.setCost(58.50);
        call2.setDuration("00:38:24");
        call2.setType("01");
        call2.setStartTime("2023-10-18 21:28:52");
        call2.setEndTime("2023-10-18 22:07:16");
        call2.setPhoneNumber(phoneNumber);

        return List.of(call1, call2);
    }

    private Tariff getTariff() {
        Tariff tariff = new Tariff();
        tariff.setId("26");
        tariff.setMonthlyFee(50.00);
        return tariff;
    }
}
