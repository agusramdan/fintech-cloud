package ramdan.project.fintech.transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ramdan.project.fintech.transfer.controller.TransferController;
import ramdan.project.fintech.transfer.domain.Account;
import ramdan.project.fintech.transfer.dto.*;
import ramdan.project.fintech.transfer.repository.AccountRepositry;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	String toJson(Object anObject) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		return ow.writeValueAsString(anObject );
	}

	@Test
	@DisplayName("Create account success")
	void create_account_success() throws Exception {
		val input = AccountDto.builder()
				.number("ADD-INT-TEST-ACCOUNT")
				.name("ADD INT TEST ACCOUNT")
				.build();

		mockMvc.perform(post("/account/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(input)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.version", is(0)))
				.andExpect(jsonPath("$.balance", is(0)))
				.andExpect(jsonPath("$.limit", is(0)))
		;

	}
	@Test
	@DisplayName("Create account have limit credit success")
	void create_accountHaveLimitCredit_success() throws Exception {
		val input = AccountDto.builder()
				.number("ADD-INT-TEST-ACC-LIMIT")
				.name("ADD INT TEST ACC LIMIT")
				.limit(BigDecimal.valueOf(100000.00))
				.build();

		mockMvc.perform(post("/account/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(input)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.limit", is(100000.00)))
		;

	}

	@Test
	void update_account_success() throws Exception {

		mockMvc.perform(get("/account/UPDATE-INT-TEST-ACCOUNT"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.version", is(3)))
				.andExpect(jsonPath("$.name", blankOrNullString()))
				.andExpect(jsonPath("$.limit", is(100000000.00)))
		;

		val input = AccountDto.builder()
				.number("UPDATE-INT-TEST-ACCOUNT")
				.name("NAME UPDATE")
				.version(3L)
				.build();

		mockMvc.perform(post("/account/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(input)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.version", is(4)))
				.andExpect(jsonPath("$.name", is("NAME UPDATE")))
				.andExpect(jsonPath("$.limit", is(100000000.00)))
		;

	}

	@Test
	void update_account_fail() throws Exception {

		val input = AccountDto.builder()
				.number("UPDATE-TEST-FAIL")
				.version(2L)
				.build();

		mockMvc.perform(post("/account/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(input)))
				.andExpect(status().isConflict())
				//.andExpect(jsonPath("$.version", is(4)))
				//.andExpect(jsonPath("$.name", is("NAME UPDATE")))
				//.andExpect(jsonPath("$.limit", is(100000000.00)))
		;

	}

	@Test
	void update_account_BalanceNotChange() throws Exception {

		val input = AccountDto.builder()
				.number("UPDATE-INT-BAL-NOT-CHANGE")
				.balance(BigDecimal.valueOf(12345678.00))
				.version(3L)
				.build();

		mockMvc.perform(post("/account/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content(toJson(input)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.balance", is(11.00)))
		//.andExpect(jsonPath("$.name", is("NAME UPDATE")))
		//.andExpect(jsonPath("$.limit", is(100000000.00)))
		;

	}
	//UPDATE-INT-TEST-ACCOUNT
//	@Autowired
//	private AccountRepositry accountRepositry;
//
//	@Autowired
//	private TransferController transferController;
//
//	@Test
//	@DisplayName("TransferCommand money success")
//	@Transactional(REQUIRED)
//	void transfer_Money_success(){
//		//accountRepositry.getOne("123456789").getBalance();
//		val source = accountRepositry.getOne("123456789").getBalance();
//		val beneficiary = accountRepositry.getOne("234567891").getBalance();
//		val input = TransferCommand.builder()
//				.no("TEST-1")
//				.type(Type.TRANSFER)
//				.source("123456789")
//				.beneficiary("234567891")
//				.amount(10.0)
//				.build();
//
//		transferController.transfer(input);
//
//		assertEquals(source - 10.0,
//				accountRepositry.getOne("123456789").getBalance());
//		assertEquals( beneficiary + 10.0 ,
//				accountRepositry.getOne("234567891").getBalance());
//
//	}
//	@Test
//	@DisplayName("TransferCommand money 2 success")
//	@Transactional(REQUIRED)
//	void transfer_Money2_success(){
//
//
//		val input = TransferCommand.builder()
//				.no("TEST-2")
//				.type(Type.TRANSFER)
//				.date(new Date())
//				.source("123456789")
//				.beneficiary("234567891")
//				.amount(10.0)
//				.remark1("r1")
//				.remark1("r2")
//				.build();
//
//		val trx = transferController.transfer(input).getBody();
//
//		val result = transferController.getJournal("TEST-2").getBody();
//
//		val source = accountRepositry.getOne("123456789").getBalance();
//		val beneficiary = accountRepositry.getOne("234567891").getBalance();
//
//		assertEquals(
//				JournalDto
//						.builder()
//						.number("TEST-2")
//						.date(trx.getDate())
//						.remark1(trx.getRemark1())
//						.remark2(trx.getRemark2())
//						.details(
//								new DetailDto[]{
//										DetailDto.builder()
//												.number(trx.getNo())
//												.account("123456789")
//												.date(trx.getDate())
//												.amount(-10.0)
//												.balance(source)
//												.remark1(trx.getRemark1())
//												.remark2(trx.getRemark2())
//												.build(),
//										DetailDto.builder()
//												.number(trx.getNo())
//												.account("234567891")
//												.date(trx.getDate())
//												.amount(10.0)
//												.balance(beneficiary)
//												.remark1(trx.getRemark1())
//												.remark2(trx.getRemark2())
//												.build()
//								}
//						)
//						.build()
//				,result);
//	}
}
