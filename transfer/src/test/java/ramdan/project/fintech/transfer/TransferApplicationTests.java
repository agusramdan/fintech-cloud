package ramdan.project.fintech.transfer;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ramdan.project.fintech.transfer.controller.TransferController;
import ramdan.project.fintech.transfer.dto.Status;
import ramdan.project.fintech.transfer.dto.Transfer;
import ramdan.project.fintech.transfer.dto.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransferApplicationTests {

	@Autowired
	private TransferController transferController;

	@Test
	@DisplayName("Transfer data success")
	void transfer_Data_success(){

		val source = transferController.balance("123456789");
		val beneficiary = transferController.balance("234567891");
		val input = Transfer.builder()
				.no("TEST-1")
				.type(Type.TRANSFER)
				.source("123456789")
				.beneficiary("234567891")
				.amount(10.0)
				.build();

		transferController.transfer(input);

		assertEquals(source - 10.0, transferController.balance("123456789"));
		assertEquals( beneficiary + 10.0 , transferController.balance("234567891"));

	}

}
