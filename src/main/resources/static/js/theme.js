(function () {
	var storedTheme = localStorage.getItem('theme');
	var preferredTheme = window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
	document.documentElement.setAttribute('data-bs-theme', storedTheme || preferredTheme);
})();

function alternarTema() {
	var currentTheme = document.documentElement.getAttribute('data-bs-theme') || 'light';
	var nextTheme = currentTheme === 'dark' ? 'light' : 'dark';
	document.documentElement.setAttribute('data-bs-theme', nextTheme);
	localStorage.setItem('theme', nextTheme);
}
