package com.daloji.blockchain.core.validation;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.daloji.blockchain.core.Block;

public class BlockConstraintValidator implements ConstraintValidator<BlockConstraint, Block> {

	private static final String ATTRIBUT_COND_MANQUANT ="attribut obligatoire manqant";
	
	@Override
	public boolean isValid(Block block, ConstraintValidatorContext context) {
	

		if(isNull(block)) {
			return true;
		}
		boolean valid = true;
		context.disableDefaultConstraintViolation();
		if(isBlank(block.getPrevBlockHash())){
			context.buildConstraintViolationWithTemplate(ATTRIBUT_COND_MANQUANT).addPropertyNode("prevBlockHash").addConstraintViolation();
			 valid &= false;
		}

		if(isBlank(block.getMerkleRoot())){
			context.buildConstraintViolationWithTemplate(ATTRIBUT_COND_MANQUANT).addPropertyNode("merkleRoot").addConstraintViolation();
			 valid &= false;
		}

		if(block.getVersion()>=1  && block.getVersion()<5){
			context.buildConstraintViolationWithTemplate(ATTRIBUT_COND_MANQUANT).addPropertyNode("version").addConstraintViolation();
			 valid &= false;
		}

		return valid;
	}

}
