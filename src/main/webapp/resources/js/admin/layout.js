function handleAjaxEvent(data) {
  if (data.status === 'begin') {
    showLoading(data);
  }
  if (data.status === 'complete' || data.status === 'success') {
    hideLoading();
  }
}

function showLoading(data) {
  const overlay = document.getElementById('loading-overlay');
  overlay.classList.remove('hide');
  overlay.classList.add('show');
}

function hideLoading() {
  const overlay = document.getElementById('loading-overlay');
  overlay.classList.remove('show');
  overlay.classList.add('hide');
  overlay.classList.remove('loading-state');
}

function hideAlert(element) {
  element.classList.remove('show');
  element.classList.add('hide');
}

function toggleSidebar() {
  const sidebar = document.getElementById('sidebar');
  if (sidebar) {
    sidebar.classList.toggle('open');
  }
}

// Fechar sidebar com a tecla ESC
document.addEventListener('keydown', (event) => {
  if (event.key === 'Escape') {
    const sidebar = document.getElementById('sidebar');
    if (sidebar && sidebar.classList.contains('open')) {
      sidebar.classList.remove('open');
    }
  }
});
