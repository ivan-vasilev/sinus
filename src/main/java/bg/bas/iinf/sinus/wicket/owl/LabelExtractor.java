package bg.bas.iinf.sinus.wicket.owl;

import org.apache.wicket.Session;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitor;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;

/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * <p>Simple visitor that grabs any labels on an entity.</p>
 * <p/>
 * Author: Sean Bechhofer<br>
 * The University Of Manchester<br>
 * Information Management Group<br>
 * Date: 17-03-2007<br>
 * <br>
 */
public class LabelExtractor implements OWLAnnotationObjectVisitor {

    private String result;

    public LabelExtractor() {
        result = null;
    }

    @Override
    public void visit(OWLAnonymousIndividual individual) {
    }

    @Override
    public void visit(IRI iri) {
    }

    @Override
    public void visit(OWLLiteral literal) {
    }

    @Override
    public void visit(OWLAnnotation annotation) {
        /*
        * If it's a label, grab it as the result. Note that if there are
        * multiple labels, the last one will be used.
        */
        if (annotation.getProperty().isLabel()) {
            OWLLiteral c = (OWLLiteral) annotation.getValue();
            String country = Session.get().getLocale().getCountry();
            if (country.equalsIgnoreCase(c.getLang())) {
            	result = c.getLiteral();
            }
        }

    }

    @Override
    public void visit(OWLAnnotationAssertionAxiom axiom) {
    }

    @Override
    public void visit(OWLAnnotationPropertyDomainAxiom axiom) {
    }

    @Override
    public void visit(OWLAnnotationPropertyRangeAxiom axiom) {
    }

    @Override
    public void visit(OWLSubAnnotationPropertyOfAxiom axiom) {
    }



    public String getResult() {
        return result;
    }
}
