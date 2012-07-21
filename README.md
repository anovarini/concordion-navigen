concordion-navigen
==================

Concordion extension for automatic generation of links to tests

The aim of this project is to create a Concordion extension that includes into a
properly instrumented page the list of tests present in the same directory.

This can be helpful when you want to use the breadcrumbs feature of Concordion for navigating
back to the parent page but you can't dig into unless you manually add the missing links.

The idea is that, in order to let navigen to add the links, the page should contain an
html tag (like, for example)

	<div id="navigen" />
	
When such placeholder is found, navigen scans the same directory of the inspected file,
and adds all the html pages that are actually acceptance tests.
