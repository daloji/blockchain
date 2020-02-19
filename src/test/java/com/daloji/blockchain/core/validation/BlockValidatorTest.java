package com.daloji.blockchain.core.validation;

import static org.junit.Assert.fail;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.daloji.blockchain.core.Block;

public class BlockValidatorTest {
	/**
	 * The validator
	 */
	private static Validator validator;


	//@BeforeClass
	public static void setUpBeforeClass()
	{

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();

	}

	//@Test
	public void test001(){
		Block block = new Block();
		block.setMerkleRoot("");

		int expectedViolatedConstraints = 5;

		final Set<ConstraintViolation<Block>> constraintViolations = validator.validate(block);
		for (ConstraintViolation<Block> constraintViolation : constraintViolations)
		{
			switch (getCanonicalName(constraintViolation))
			{
		

			default:
				fail("Should not append"); //$NON-NLS-1$
			}
		}
	}


	/**
	 * @param constraintValidation_p
	 *          constraintValidation
	 * @return String
	 */
	private String getCanonicalName(final ConstraintViolation<?> constraintValidation_p){
		//return Stream.of(constraintValidation_p.getRootBeanClass().getSimpleName(), constraintValidation_p.getPropertyPath().toString()).filter(s -> !isBlank(s)).collect(Collectors.joining(".")); //$NON-NLS-1$
	return null;
	}
}
