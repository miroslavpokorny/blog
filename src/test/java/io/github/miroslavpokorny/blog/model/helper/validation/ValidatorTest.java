package io.github.miroslavpokorny.blog.model.helper.validation;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class ValidatorTest {

    @Test
    public void sameAsTest() throws Exception {
        Assert.assertFalse(Validator.sameAs("", null).isValid());
        Assert.assertFalse(Validator.sameAs("", " ").isValid());
        Assert.assertFalse(Validator.sameAs("", " ").isValid());
        Assert.assertFalse(Validator.sameAs("aa", "bb").isValid());
        Assert.assertTrue(Validator.sameAs(null, null).isValid());
        Assert.assertFalse(Validator.sameAs(null, "").isValid());
        Assert.assertTrue(Validator.sameAs("a", "a").isValid());

    }

    @Test
    public void emailTest() throws Exception {
        Assert.assertFalse(Validator.email("").isValid());
        Assert.assertFalse(Validator.email(null).isValid());
        Assert.assertFalse(Validator.email(" ").isValid());
        Assert.assertTrue(Validator.email("a@a.a").isValid());
        Assert.assertTrue(Validator.email("a@a.aa").isValid());
    }

    @Test
    public void notEmptyTest() throws Exception {
        Assert.assertFalse(Validator.notEmpty("").isValid());
        Assert.assertFalse(Validator.notEmpty(" ").isValid());
        Assert.assertFalse(Validator.notEmpty(null).isValid());
        Assert.assertTrue(Validator.notEmpty("a").isValid());
    }

    @Test
    public void multipleValidationsTest() throws Exception {
        Assert.assertFalse(Validator.notEmpty("").email().isValid());
        Assert.assertFalse(Validator.notEmpty("abc").email().isValid());
        Assert.assertTrue(Validator.notEmpty("a@a.aa").email().isValid());
    }
}
